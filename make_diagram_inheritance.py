import os
import glob
import javalang
from graphviz import Digraph
import sys
import argparse

class ExpandedJavaAnalyzer:
    def __init__(self):
        self.classes = {}
        self.relationships = []
    
    def analyze_project(self, project_path):
        """Recursively analyze Java files"""
        java_files = glob.glob(os.path.join(project_path, "**", "*.java"), recursive=True)
        
        for java_file in java_files:
            self.analyze_java_file(java_file)
    
    def analyze_java_file(self, file_path):
        """Parse Java file with error handling"""
        try:
            with open(file_path, 'r', encoding='utf-8', errors='ignore') as file:
                content = file.read()
            
            tree = javalang.parse.parse(content)
            package = tree.package.name if tree.package else "default"
            
            for type_decl in tree.types:
                self.analyze_type_declaration(type_decl, package, file_path)
                
        except Exception as e:
            print(f"Error parsing {file_path}: {str(e)[:100]}...")
    
    def analyze_type_declaration(self, type_decl, package, file_path):
        """Analyze class/interface"""
        try:
            class_name = type_decl.name
            full_class_name = f"{package}.{class_name}"
            
            class_info = {
                'name': class_name,
                'full_name': full_class_name,
                'is_interface': isinstance(type_decl, javalang.tree.InterfaceDeclaration),
                'package': package,
                'file_path': file_path
            }
            
            # Handle inheritance
            if hasattr(type_decl, 'extends') and type_decl.extends:
                if isinstance(type_decl.extends, list):
                    for parent in type_decl.extends:
                        if hasattr(parent, 'name'):
                            self.relationships.append(('EXTENDS', full_class_name, parent.name))
                else:
                    if hasattr(type_decl.extends, 'name'):
                        self.relationships.append(('EXTENDS', full_class_name, type_decl.extends.name))
            
            # Handle implementations
            if hasattr(type_decl, 'implements') and type_decl.implements:
                for interface in type_decl.implements:
                    if hasattr(interface, 'name'):
                        self.relationships.append(('IMPLEMENTS', full_class_name, interface.name))
            
            self.classes[full_class_name] = class_info
            
        except Exception as e:
            print(f"Error analyzing type in {file_path}: {e}")
    
    def get_class_name_from_file(self, file_path):
        """Extract fully qualified class name from file path"""
        try:
            with open(file_path, 'r', encoding='utf-8', errors='ignore') as file:
                content = file.read()
            
            tree = javalang.parse.parse(content)
            package = tree.package.name if tree.package else "default"
            
            for type_decl in tree.types:
                if hasattr(type_decl, 'name'):
                    return f"{package}.{type_decl.name}"
            
            return None
        except Exception as e:
            print(f"Error extracting class name from {file_path}: {e}")
            return None
    
    def generate_global_diagram(self, output_file='global_diagram'):
        """Generate a large, detailed global diagram"""
        if not self.classes:
            print("No classes found to generate diagram")
            return
        
        dot = Digraph(comment='Java Class Diagram - Global View', format='png')
        
        # Very large paper size for global view
        dot.attr(size="100,100")  # Massive paper size
        dot.attr(ratio="compress") 
        dot.attr(ranksep="4.0")  # Even more space between ranks
        dot.attr(nodesep="1.5")  # More space between nodes
        dot.attr(splines="ortho") 
        dot.attr(concentrate="false")
        dot.attr(overlap="false")  # Prevent overlap
        dot.attr(fontsize="12")
        
        # Use dot layout for hierarchical view
        dot.attr(layout="dot")
        
        # Group by package with better visual grouping
        packages = {}
        for class_name, info in self.classes.items():
            pkg = info['package']
            if pkg not in packages:
                packages[pkg] = []
            packages[pkg].append(class_name)
        
        print(f"Generating global diagram with {len(self.classes)} classes in {len(packages)} packages")
        
        # Create subgraphs for each package
        for pkg_name, class_list in packages.items():
            with dot.subgraph(name=f'cluster_{pkg_name}') as pkg_cluster:
                pkg_cluster.attr(label=pkg_name, style='rounded,filled', 
                               color='lightblue', fillcolor='lightblue:lightgray',
                               gradientangle='90', fontsize='14', fontname='bold')
                
                for class_name in class_list:
                    info = self.classes[class_name]
                    label = self.create_detailed_class_label(info)
                    
                    # Different styles for interfaces vs classes
                    if info['is_interface']:
                        pkg_cluster.node(class_name, label, shape='component', 
                                       fontsize='10', width='2.5', height='1.2',
                                       style='filled', fillcolor='lightgreen',
                                       color='darkgreen')
                    else:
                        pkg_cluster.node(class_name, label, shape='record', 
                                       fontsize='10', width='2.5', height='1.2',
                                       style='filled', fillcolor='lightyellow',
                                       color='darkgoldenrod')
        
        # Add relationships with better styling
        for rel_type, from_class, to_class in self.relationships:
            if from_class in self.classes:
                resolved_to = self.resolve_class_name(to_class, self.classes[from_class]['package'])
                
                if rel_type == 'EXTENDS':
                    dot.edge(from_class, resolved_to, color='red', style='bold', 
                           arrowhead='onormal', arrowsize='1.5', label=' extends',
                           fontsize='10', fontcolor='red')
                elif rel_type == 'IMPLEMENTS':
                    dot.edge(from_class, resolved_to, color='blue', style='dashed', 
                           arrowhead='onormal', arrowsize='1.5', label=' implements',
                           fontsize='10', fontcolor='blue')
        
        # Render the diagram
        dot.render(output_file, cleanup=True, view=False)
        print(f"Global diagram generated: {output_file}.png")
        
        # Generate SVG version for better zooming
        dot.format = 'svg'
        svg_output = output_file + '_detailed'
        dot.render(svg_output, cleanup=True)
        print(f"Detailed SVG version: {svg_output}.svg (better for zooming)")
    
    def create_detailed_class_label(self, class_info):
        """Create a more detailed label for global diagram"""
        base_label = f"«interface»\\n{class_info['name']}" if class_info['is_interface'] else class_info['name']
        return base_label
    
    def resolve_class_name(self, short_name, current_package):
        """Try to resolve short class name to full name"""
        # Check if it's in the same package
        potential_full = f"{current_package}.{short_name}"
        if potential_full in self.classes:
            return potential_full
        
        # Check if it matches any class name (case-insensitive partial match)
        for full_name in self.classes.keys():
            if short_name.lower() in full_name.lower():
                return full_name
        
        return short_name
    
    def generate_focused_diagram(self, target_file_path, output_file=None):
        """Generate diagram focusing on a specific Java file and its relationships"""
        # Get the class name from the target file
        main_class = self.get_class_name_from_file(target_file_path)
        
        if not main_class:
            print(f"Could not extract class name from {target_file_path}")
            print("Available Java files in project:")
            java_files = glob.glob(os.path.join(".", "**", "*.java"), recursive=True)
            for f in java_files[:10]:  # Show first 10
                print(f"  - {f}")
            return
        
        if main_class not in self.classes:
            print(f"Class {main_class} not found in analyzed classes!")
            print("Available classes:")
            for cls in list(self.classes.keys())[:10]:
                print(f"  - {cls}")
            return
        
        # Set default output filename based on class name
        if output_file is None:
            class_simple_name = main_class.split('.')[-1]
            output_file = f"focused_{class_simple_name}"
        
        print(f"Generating focused diagram for: {main_class}")
        print(f"Source file: {target_file_path}")
        
        dot = Digraph(comment=f'Focused Diagram - {main_class}', format='png')
        
        # Optimized layout for focused view
        dot.attr(size="24,24", ranksep="2.5", nodesep="1.2", 
                splines="true", concentrate="false", fontsize="11")
        
        # Start with main class
        main_info = self.classes[main_class]
        dot.node(main_class, self.create_detailed_class_label(main_info), 
                shape='record' if not main_info['is_interface'] else 'component',
                color='red', style='filled', fillcolor='lightcoral',
                fontsize='12', width='2.2', height='1.1')
        
        # Find related classes (more comprehensive search)
        related_classes = set([main_class])
        
        # Multi-level relationship discovery
        for _ in range(3):  # Discover up to 3 levels deep
            new_related = set()
            for rel_type, from_class, to_class in self.relationships:
                # Check both directions
                if from_class in related_classes:
                    resolved_to = self.resolve_class_name(to_class, main_info['package'])
                    if resolved_to not in related_classes:
                        new_related.add(resolved_to)
                
                if to_class in related_classes or any(cls.endswith(to_class) for cls in related_classes):
                    if from_class not in related_classes:
                        new_related.add(from_class)
            
            if not new_related:
                break
            related_classes.update(new_related)
        
        print(f"Found {len(related_classes)} related classes")
        
        # Add related classes to diagram with different styles
        for class_name in related_classes:
            if class_name in self.classes:
                info = self.classes[class_name]
                label = self.create_detailed_class_label(info)
                
                if class_name == main_class:
                    # Main class - highlighted
                    dot.node(class_name, label, 
                            shape='record' if not info['is_interface'] else 'component',
                            color='red', style='filled', fillcolor='lightcoral',
                            fontsize='12')
                else:
                    # Related classes - different styles based on type
                    if info['is_interface']:
                        dot.node(class_name, label, shape='component',
                                style='filled', fillcolor='lightgreen')
                    else:
                        dot.node(class_name, label, shape='record',
                                style='filled', fillcolor='lightyellow')
        
        # Add relationships between related classes
        relationship_count = 0
        for rel_type, from_class, to_class in self.relationships:
            if from_class in related_classes:
                resolved_to = self.resolve_class_name(to_class, main_info['package'])
                if resolved_to in related_classes:
                    relationship_count += 1
                    if rel_type == 'EXTENDS':
                        dot.edge(from_class, resolved_to, color='red', style='bold', 
                               arrowhead='onormal', label=' extends')
                    elif rel_type == 'IMPLEMENTS':
                        dot.edge(from_class, resolved_to, color='blue', style='dashed', 
                               arrowhead='onormal', label=' implements')
        
        print(f"Showing {relationship_count} relationships")
        
        dot.render(output_file, cleanup=True)
        print(f"Focused diagram generated: {output_file}.png")
        
        # Also generate SVG for focused diagram
        dot.format = 'svg'
        dot.render(output_file + '_svg', cleanup=True)
        print(f"Focused SVG diagram: {output_file}_svg.svg")

def main():
    parser = argparse.ArgumentParser(description='Generate Java class relationship diagrams')
    parser.add_argument('project_path', nargs='?', default='./', 
                       help='Path to Java project (default: current directory)')
    parser.add_argument('--focus', '-f', 
                       help='Path to specific Java file for focused diagram')
    parser.add_argument('--output', '-o', 
                       help='Output filename for focused diagram (without extension)')
    parser.add_argument('--global-only', action='store_true',
                       help='Generate only global diagram')
    parser.add_argument('--focus-only', action='store_true',
                       help='Generate only focused diagram')
    
    args = parser.parse_args()
    
    analyzer = ExpandedJavaAnalyzer()
    
    print(f"Analyzing Java project: {args.project_path}")
    analyzer.analyze_project(args.project_path)
    
    print(f"Analysis complete: {len(analyzer.classes)} classes, {len(analyzer.relationships)} relationships")
    
    # Generate global diagram unless focus-only is specified
    if not args.focus_only:
        analyzer.generate_global_diagram("global_diagram")
    
    # Generate focused diagram if a file is specified and not global-only
    if args.focus and not args.global_only:
        if os.path.exists(args.focus):
            output_name = args.output if args.output else None
            analyzer.generate_focused_diagram(args.focus, output_name)
        else:
            print(f"Error: File not found: {args.focus}")
            print("Available Java files:")
            java_files = glob.glob(os.path.join(args.project_path, "**", "*.java"), recursive=True)
            for f in java_files[:15]:  # Show first 15
                print(f"  - {f}")
    
    # If no focus file specified, show available classes for focused analysis
    if not args.focus and not args.focus_only:
        print("\nAvailable classes for focused diagrams (first 15):")
        for i, class_name in enumerate(list(analyzer.classes.keys())[:15]):
            class_info = analyzer.classes[class_name]
            print(f"  {i+1:2d}. {class_name} ({class_info['file_path']})")
        
        print("\nTo generate a focused diagram, use: python make_diagram.py <project_path> --focus <java_file_path>")
        print("Example: python make_diagram.py . --focus ./src/main/java/com/example/MyClass.java")

if __name__ == "__main__":
    main()
