#!/usr/bin/env python3
"""
Setup script for local Qwen3-Next-80B-A3B-Thinking model
This script helps you install and test the local AI model for the PESTEL simulation
"""

import subprocess
import sys
import os

def install_requirements():
    """Install required Python packages"""
    print("🔧 Installing required packages...")
    
    packages = [
        "torch",
        "transformers>=4.46.0",
        "accelerate",
        "bitsandbytes",  # For quantization
        "flash-attn"     # For efficiency (optional)
    ]
    
    for package in packages:
        try:
            print(f"Installing {package}...")
            subprocess.check_call([sys.executable, "-m", "pip", "install", package])
            print(f"✅ {package} installed successfully")
        except subprocess.CalledProcessError as e:
            print(f"⚠️ Failed to install {package}: {e}")
            if package == "flash-attn":
                print("   (flash-attn is optional - continuing without it)")
            else:
                return False
    
    return True

def download_model():
    """Download and cache the Qwen3-Next model"""
    print("\n📥 Downloading Qwen3-Next-80B-A3B-Thinking model...")
    print("⚠️ WARNING: This model is ~160GB and requires significant GPU memory!")
    print("   Recommended: 24GB+ GPU memory or CPU with 64GB+ RAM")
    
    response = input("Continue with download? (y/N): ").lower()
    if response != 'y':
        print("❌ Model download cancelled")
        return False
    
    try:
        from transformers import AutoTokenizer, AutoModelForCausalLM
        import torch
        
        model_name = "Qwen/Qwen3-Next-80B-A3B-Thinking"
        
        print("📦 Downloading tokenizer...")
        tokenizer = AutoTokenizer.from_pretrained(model_name)
        print("✅ Tokenizer downloaded")
        
        print("📦 Downloading model (this may take a while)...")
        model = AutoModelForCausalLM.from_pretrained(
            model_name,
            torch_dtype=torch.bfloat16,
            device_map="auto",
            trust_remote_code=True
        )
        print("✅ Model downloaded and cached")
        
        return True
        
    except Exception as e:
        print(f"❌ Model download failed: {e}")
        return False

def test_model():
    """Test the local model"""
    print("\n🧪 Testing local model...")
    
    try:
        from transformers import AutoTokenizer, AutoModelForCausalLM
        import torch
        
        model_name = "Qwen/Qwen3-Next-80B-A3B-Thinking"
        
        print("Loading model for test...")
        tokenizer = AutoTokenizer.from_pretrained(model_name)
        model = AutoModelForCausalLM.from_pretrained(
            model_name,
            torch_dtype=torch.bfloat16,
            device_map="auto",
            trust_remote_code=True
        )
        
        # Test prompt
        test_prompt = "Should Apple invest in quantum computing? Answer YES or NO with brief reason."
        
        messages = [{"role": "user", "content": test_prompt}]
        text = tokenizer.apply_chat_template(messages, tokenize=False, add_generation_prompt=True)
        model_inputs = tokenizer([text], return_tensors="pt").to(model.device)
        
        print("Generating test response...")
        with torch.no_grad():
            generated_ids = model.generate(
                **model_inputs,
                max_new_tokens=256,
                temperature=0.6,
                top_p=0.95,
                do_sample=True,
                pad_token_id=tokenizer.eos_token_id
            )
        
        output_ids = generated_ids[0][len(model_inputs.input_ids[0]):].tolist()
        response = tokenizer.decode(output_ids, skip_special_tokens=True)
        
        print("✅ Model test successful!")
        print(f"Test response: {response}")
        return True
        
    except Exception as e:
        print(f"❌ Model test failed: {e}")
        return False

def create_lightweight_version():
    """Create a lightweight version using smaller model"""
    print("\n💡 Setting up lightweight version with smaller model...")
    
    # You can use a smaller Qwen model for testing
    smaller_models = [
        "Qwen/Qwen2.5-7B-Instruct",  # Much smaller, good for testing
        "Qwen/Qwen2.5-14B-Instruct", # Medium size
        "Qwen/Qwen2.5-32B-Instruct"  # Larger but still manageable
    ]
    
    print("Available lighter models:")
    for i, model in enumerate(smaller_models):
        print(f"  {i+1}. {model}")
    
    choice = input("Choose model (1-3) or press Enter for simulation without AI: ")
    
    if choice in ['1', '2', '3']:
        selected_model = smaller_models[int(choice) - 1]
        print(f"📥 Downloading {selected_model}...")
        
        try:
            from transformers import AutoTokenizer, AutoModelForCausalLM
            
            tokenizer = AutoTokenizer.from_pretrained(selected_model)
            model = AutoModelForCausalLM.from_pretrained(
                selected_model,
                torch_dtype="auto",
                device_map="auto"
            )
            
            print(f"✅ {selected_model} ready for use!")
            print(f"💡 Update your Java code to use: {selected_model}")
            return True
            
        except Exception as e:
            print(f"❌ Failed to setup {selected_model}: {e}")
            return False
    
    print("📋 Running simulation without AI - using advanced fallback logic")
    return True

def main():
    print("=== QWEN3-NEXT LOCAL SETUP FOR PESTEL SIMULATION ===")
    print("Metropolia University - AI Strategic Planning System")
    print()
    
    # Check system requirements
    print("🔍 Checking system requirements...")
    
    try:
        import torch
        print(f"✅ PyTorch available: {torch.__version__}")
        
        if torch.cuda.is_available():
            gpu_count = torch.cuda.device_count()
            gpu_memory = torch.cuda.get_device_properties(0).total_memory / 1e9
            print(f"✅ CUDA available: {gpu_count} GPU(s), {gpu_memory:.1f}GB memory")
            
            if gpu_memory < 20:
                print("⚠️ WARNING: Limited GPU memory. Consider using smaller model or CPU mode.")
        else:
            print("⚠️ CUDA not available - will use CPU (slower)")
            
    except ImportError:
        print("❌ PyTorch not found")
        if not install_requirements():
            print("❌ Setup failed")
            return
    
    print("\n📋 Setup Options:")
    print("1. Full Qwen3-Next-80B model (requires 24GB+ GPU)")
    print("2. Lightweight version (7B-32B models)")
    print("3. No AI - advanced fallback only")
    
    choice = input("\nChoose option (1-3): ").strip()
    
    if choice == "1":
        if install_requirements() and download_model():
            test_model()
    elif choice == "2":
        if install_requirements():
            create_lightweight_version()
    else:
        print("✅ Using advanced fallback logic - no AI model needed")
        print("🚀 You can run the simulation immediately with:")
        print("   java -cp . test.FixedRealWorldTest")
    
    print("\n=== SETUP COMPLETE ===")
    print("🎯 Your PESTEL simulation is ready!")
    print("\n📋 Commands to run:")
    print("   java -cp . test.FixedRealWorldTest                    # Advanced fallback mode")
    print("   java -DLOCAL_AI=true -cp . test.FixedRealWorldTest    # With local AI (if installed)")

if __name__ == "__main__":
    main()
