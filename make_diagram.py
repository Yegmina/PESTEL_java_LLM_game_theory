import os
import sys
import re
import glob
from pathlib import Path
import graphviz

class JavaDependencyAnalyzer:
    def __init__(self):
        self.java_files = {}
        self.dependencies = {}
        
    def find_all_java_files(self, start_path):
        """Find all Java files recursively from start path"""
        java_files = {}
        for java_file in glob.glob(os.path.join(start_path, "**", "*.java"), recursive=True):
            class_name = Path(java_file).stem
            # Use relative path as key to handle same class names in different packages
            rel_path = os.path.relpath(java_file, start_path)
            java_files[rel_path] = java_file
        return java_files
    
    def extract_java_references(self, file_path):
        """Extract all Java class references from a file"""
        references = set()
        try:
            with open(file_path, 'r', encoding='utf-8', errors='ignore') as f:
                content = f.read()
                
                # Remove comments to avoid false positives
                content = self.remove_comments(content)
                
                # Find class declarations (extends, implements)
                class_patterns = [
                    r'class\s+(\w+)\s+extends\s+(\w+)',
                    r'class\s+(\w+)\s+implements\s+([\w\s,]+)',
                    r'extends\s+(\w+)',
                    r'implements\s+([\w\s,]+)',
                    r'new\s+(\w+)\s*\(',
                    r'(\w+)\.class',
                    r'@(\w+)',
                    r'import\s+([\w\.]+);'
                ]
                
                for pattern in class_patterns:
                    matches = re.findall(pattern, content)
                    for match in matches:
                        if isinstance(match, tuple):
                            for item in match:
                                if item:
                                    refs = [ref.strip() for ref in item.split(',')]
                                    references.update(refs)
                        else:
                            references.add(match)
                
                # Find method calls (anything.anything()
                method_pattern = r'(\w+)\.\w+\s*\('
                method_matches = re.findall(method_pattern, content)
                references.update(method_matches)
                
                # Find attribute access (anything.anything without parentheses)
                attr_pattern = r'(\w+)\.\w+(\s|;|\)|,|\]|\[)'
                attr_matches = re.findall(attr_pattern, content)
                for match in attr_matches:
                    if match[0]:
                        references.add(match[0])
                        
        except Exception as e:
            print(f"Error reading {file_path}: {e}")
            
        return {ref for ref in references if ref and ref[0].isupper() and ref not in ['String', 'System', 'Object', 'Integer', 'Boolean']}
    
    def remove_comments(self, content):
        """Remove Java comments from content"""
        # Remove block comments
        content = re.sub(r'/\*.*?\*/', '', content, flags=re.DOTALL)
        # Remove line comments
        content = re.sub(r'//.*', '', content)
        return content
    
    def find_matching_java_files(self, class_name, java_files):
        """Find Java files that match a class name"""
        matches = []
        for file_path in java_files.values():
            file_name = Path(file_path).stem
            if file_name == class_name:
                matches.append(file_path)
        return matches
    
    def build_dependency_graph(self, base_file):
        """Build complete dependency graph"""
        start_dir = os.path.dirname(base_file) or '.'
        self.java_files = self.find_all_java_files(start_dir)
        
        print(f"Found {len(self.java_files)} Java files")
        
        # Build dependency mapping
        for rel_path, abs_path in self.java_files.items():
            references = self.extract_java_references(abs_path)
            file_deps = set()
            
            for ref in references:
                # Try exact filename match first
                matching_files = self.find_matching_java_files(ref, self.java_files)
                for match in matching_files:
                    match_rel = os.path.relpath(match, start_dir)
                    if match_rel != rel_path:  # Don't point to self
                        file_deps.add(match_rel)
            
            self.dependencies[rel_path] = file_deps
        
        return self.dependencies
    
    def create_text_diagram(self, graph, start_file):
        """Create text-based diagram"""
        start_rel = os.path.relpath(start_file, os.path.dirname(start_file) or '.')
        visited = set()
        
        def build_tree(node, level=0, is_last=False):
            if node in visited:
                return f"{'    ' * level}└── {node} (cyclic reference)\n"
            visited.add(node)
            
            prefix = "    " * level
            connector = "└── " if is_last else "├── "
            result = f"{prefix}{connector}{node}\n"
            
            deps = sorted(graph.get(node, []))
            for i, dep in enumerate(deps):
                is_last_dep = (i == len(deps) - 1)
                result += build_tree(dep, level + 1, is_last_dep)
                
            return result
        
        return build_tree(start_rel)
    
    def create_svg_diagram(self, graph, start_file, output_name="java_dependencies"):
        """Create SVG diagram using Graphviz"""
        start_rel = os.path.relpath(start_file, os.path.dirname(start_file) or '.')
        
        dot = graphviz.Digraph(comment='Java Dependencies', format='svg')
        dot.attr(rankdir='TB', size='20,20')
        dot.attr('node', shape='box', style='filled', fillcolor='lightblue')
        dot.attr('edge', arrowhead='vee')
        
        visited = set()
        
        def add_nodes_edges(node):
            if node in visited:
                return
            visited.add(node)
            
            dot.node(node, node)
            
            for dep in sorted(graph.get(node, [])):
                dot.edge(node, dep)
                add_nodes_edges(dep)
        
        add_nodes_edges(start_rel)
        
        # Save SVG file
        svg_filename = dot.render(output_name, cleanup=True)
        print(f"SVG diagram saved as: {svg_filename}")
        return svg_filename

def main():
    if len(sys.argv) < 2:
        print("Usage: python java_diagram.py <base_java_file> [output_name]")
        sys.exit(1)
    
    base_file = sys.argv[1]
    output_name = sys.argv[2] if len(sys.argv) > 2 else "java_dependencies"
    
    if not os.path.exists(base_file):
        print(f"Error: File {base_file} not found")
        sys.exit(1)
    
    analyzer = JavaDependencyAnalyzer()
    
    print("Building dependency graph...")
    graph = analyzer.build_dependency_graph(base_file)
    
    print("\nText Diagram:")
    print("=" * 60)
    text_diagram = analyzer.create_text_diagram(graph, base_file)
    print(text_diagram)
    
    print("Generating SVG diagram...")
    svg_file = analyzer.create_svg_diagram(graph, base_file, output_name)
    
    print(f"\nDiagram complete! Check {svg_file} for the SVG version.")

if __name__ == "__main__":
    main()
