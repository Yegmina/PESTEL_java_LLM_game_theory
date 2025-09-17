#!/usr/bin/env python3
"""
Local Qwen Model Server for PESTEL Simulation
Serves Qwen model via HTTP API compatible with the Java application
"""

import json
import torch
from flask import Flask, request, jsonify
from transformers import AutoTokenizer, AutoModelForCausalLM
import threading
import time
import sys
import argparse

app = Flask(__name__)

class QwenServer:
    def __init__(self, model_name="Qwen/Qwen2.5-7B-Instruct"):
        self.model_name = model_name
        self.model = None
        self.tokenizer = None
        self.loading = False
        self.ready = False
        
    def load_model(self):
        """Load the model and tokenizer"""
        print(f"🔄 Loading model: {self.model_name}")
        self.loading = True
        
        try:
            # Load tokenizer
            print("📦 Loading tokenizer...")
            self.tokenizer = AutoTokenizer.from_pretrained(self.model_name)
            
            # Load model with appropriate settings
            print("📦 Loading model...")
            device_map = "auto" if torch.cuda.is_available() else "cpu"
            torch_dtype = torch.bfloat16 if torch.cuda.is_available() else torch.float32
            
            self.model = AutoModelForCausalLM.from_pretrained(
                self.model_name,
                torch_dtype=torch_dtype,
                device_map=device_map,
                trust_remote_code=True,
                low_cpu_mem_usage=True
            )
            
            print("✅ Model loaded successfully!")
            self.ready = True
            self.loading = False
            
        except Exception as e:
            print(f"❌ Failed to load model: {e}")
            self.loading = False
            self.ready = False
            
    def generate_response(self, prompt, max_tokens=200, temperature=0.7):
        """Generate response from the model"""
        if not self.ready:
            return "Model not ready"
            
        try:
            # Format the prompt
            messages = [{"role": "user", "content": prompt}]
            text = self.tokenizer.apply_chat_template(
                messages, 
                tokenize=False, 
                add_generation_prompt=True
            )
            
            # Tokenize
            model_inputs = self.tokenizer([text], return_tensors="pt")
            if torch.cuda.is_available():
                model_inputs = {k: v.to(self.model.device) for k, v in model_inputs.items()}
            
            # Generate
            with torch.no_grad():
                generated_ids = self.model.generate(
                    **model_inputs,
                    max_new_tokens=max_tokens,
                    temperature=temperature,
                    top_p=0.95,
                    do_sample=True,
                    pad_token_id=self.tokenizer.eos_token_id
                )
            
            # Decode response
            input_length = model_inputs['input_ids'].shape[1]
            output_ids = generated_ids[0][input_length:].tolist()
            response = self.tokenizer.decode(output_ids, skip_special_tokens=True)
            
            return response.strip()
            
        except Exception as e:
            print(f"❌ Generation error: {e}")
            return f"Generation failed: {str(e)}"

# Global server instance
qwen_server = QwenServer()

@app.route('/health', methods=['GET'])
def health():
    """Health check endpoint"""
    if qwen_server.loading:
        return jsonify({"status": "loading", "message": "Model is loading..."}), 202
    elif qwen_server.ready:
        return jsonify({"status": "ready", "message": "Model is ready"}), 200
    else:
        return jsonify({"status": "error", "message": "Model failed to load"}), 500

@app.route('/v1/chat/completions', methods=['POST'])
def chat_completions():
    """OpenAI-compatible chat completions endpoint"""
    if not qwen_server.ready:
        return jsonify({"error": "Model not ready"}), 503
        
    try:
        data = request.get_json()
        
        # Extract parameters
        messages = data.get('messages', [])
        max_tokens = data.get('max_tokens', 200)
        temperature = data.get('temperature', 0.7)
        
        if not messages:
            return jsonify({"error": "No messages provided"}), 400
            
        # Get the user's message
        user_message = messages[-1].get('content', '')
        
        # Generate response
        response_text = qwen_server.generate_response(
            user_message, 
            max_tokens=max_tokens, 
            temperature=temperature
        )
        
        # Format OpenAI-compatible response
        response = {
            "id": f"chatcmpl-{int(time.time())}",
            "object": "chat.completion",
            "created": int(time.time()),
            "model": qwen_server.model_name,
            "choices": [{
                "index": 0,
                "message": {
                    "role": "assistant",
                    "content": response_text
                },
                "finish_reason": "stop"
            }],
            "usage": {
                "prompt_tokens": len(user_message.split()),
                "completion_tokens": len(response_text.split()),
                "total_tokens": len(user_message.split()) + len(response_text.split())
            }
        }
        
        return jsonify(response)
        
    except Exception as e:
        print(f"❌ API error: {e}")
        return jsonify({"error": str(e)}), 500

@app.route('/generate', methods=['POST'])
def generate():
    """Simple generation endpoint"""
    if not qwen_server.ready:
        return jsonify({"error": "Model not ready"}), 503
        
    try:
        data = request.get_json()
        prompt = data.get('prompt', '')
        max_tokens = data.get('max_tokens', 200)
        temperature = data.get('temperature', 0.7)
        
        if not prompt:
            return jsonify({"error": "No prompt provided"}), 400
            
        response_text = qwen_server.generate_response(prompt, max_tokens, temperature)
        
        return jsonify({
            "response": response_text,
            "model": qwen_server.model_name
        })
        
    except Exception as e:
        return jsonify({"error": str(e)}), 500

def main():
    parser = argparse.ArgumentParser(description='Qwen Model Server for PESTEL Simulation')
    parser.add_argument('--model', default='Qwen/Qwen2.5-7B-Instruct', 
                       help='Model name (default: Qwen/Qwen2.5-7B-Instruct)')
    parser.add_argument('--port', type=int, default=8000, 
                       help='Port to run server on (default: 8000)')
    parser.add_argument('--host', default='localhost', 
                       help='Host to bind to (default: localhost)')
    
    args = parser.parse_args()
    
    print("=== QWEN MODEL SERVER FOR PESTEL SIMULATION ===")
    print(f"Model: {args.model}")
    print(f"Server: http://{args.host}:{args.port}")
    print()
    
    # Set the model name
    qwen_server.model_name = args.model
    
    # Load model in background thread
    def load_model_thread():
        qwen_server.load_model()
    
    loading_thread = threading.Thread(target=load_model_thread)
    loading_thread.daemon = True
    loading_thread.start()
    
    print("🚀 Starting server...")
    print("📋 Endpoints:")
    print(f"   GET  http://{args.host}:{args.port}/health")
    print(f"   POST http://{args.host}:{args.port}/v1/chat/completions")
    print(f"   POST http://{args.host}:{args.port}/generate")
    print()
    print("💡 Test with:")
    print(f'   curl http://{args.host}:{args.port}/health')
    print()
    
    # Run Flask app
    app.run(host=args.host, port=args.port, debug=False)

if __name__ == '__main__':
    main()
