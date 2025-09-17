# PESTEL Java LLM Game Theory - AI Forecasting System

[![Java](https://img.shields.io/badge/Java-11+-orange.svg)](https://www.oracle.com/java/)
[![Gemini API](https://img.shields.io/badge/Gemini-API-blue.svg)](https://ai.google.dev/gemini-api)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

## 🎯 Overview

An advanced AI-powered strategic planning and forecasting system designed for Metropolia University of Applied Sciences. This system combines **PESTEL analysis**, **multi-agent simulation**, **Large Language Models (LLM)**, and **Game Theory** principles to generate future scenarios for institutional strategic planning.

## 🚀 Key Features

### 🧠 **AI-Driven Decision Making**
- **Gemini API Integration** with Gemma 3 model (gemini-2.0-flash-lite)
- **Daily AI Consultations** - Each agent asks AI for strategic decisions
- **Intelligent PESTEL Analysis** - AI evaluates impact on Political, Economic, Social, Technological, Environmental, and Legal factors
- **Adaptive Agent Interactions** - AI determines which agents are affected by decisions

### 📊 **PESTEL-Based Framework**
- **Comprehensive PESTEL State Management** with text-based variables
- **Global and Local PESTEL States** for each agent type
- **Real-time PESTEL Impact Analysis** for every decision
- **Dynamic Factor Updates** based on agent actions

### 🎮 **Multi-Agent Game Theory System**
- **Three Agent Types:**
  - **🏢 Companies** - Market-driven decisions, R&D investments, strategic partnerships
  - **🏛️ Countries** - Policy decisions, diplomatic relations, economic strategies  
  - **🔬 Researchers** - Innovation projects, collaboration networks, breakthrough research

### ⚡ **Advanced Simulation Engine**
- **ABC Phase Framework** (Time, Scheduled Events, Conditional Events)
- **Day-by-Day Progression** with detailed decision tracking
- **Fallback Logic** for offline operation when AI unavailable
- **Comprehensive Results Analysis** and strategic recommendations

## 🏗️ Architecture

```
📁 PESTEL_java_LLM_game_theory/
├── 📁 simu/
│   ├── 📁 framework/          # Base simulation framework
│   │   ├── Engine.java        # ABC Phase simulation engine
│   │   ├── Clock.java         # Global simulation clock
│   │   ├── Event.java         # Event system
│   │   └── EventList.java     # Event scheduling
│   └── 📁 model/              # PESTEL simulation models
│       ├── PESTELState.java           # PESTEL factors management
│       ├── PESTELAgent.java           # Base agent class
│       ├── CompanyPESTELAgent.java    # Company behavior
│       ├── CountryPESTELAgent.java    # Country behavior
│       ├── ResearcherPESTELAgent.java # Researcher behavior
│       ├── PESTELAIService.java       # Gemini API integration
│       ├── PESTELSimulationEngine.java # Main simulation engine
│       ├── AgentDecision.java         # Decision representation
│       ├── AgentAction.java           # Action tracking
│       └── PESTELChange.java          # Change tracking
├── 📁 test/
│   ├── PESTELSimulationTest.java      # Main test runner
│   ├── GlobalWarPredictionTest.java   # Conflict scenario testing
│   └── SimpleForecastingDemo.java     # Basic demo
├── 📁 eduni/
│   └── 📁 distributions/      # Statistical distributions library
└── README.md
```

## 🔄 Simulation Workflow

### Daily Decision Process:
1. **🌅 Day X Begins**
2. **🤖 For Each Agent:**
   - AI asks: *"Do you want to take action today?"*
   - If **NO** → Record "no action"
   - If **YES** → Generate specific action description
3. **📈 PESTEL Impact Analysis:**
   - **P** - How does this affect **Political** factors?
   - **E** - How does this affect **Economic** factors?
   - **S** - How does this affect **Social** factors?
   - **T** - How does this affect **Technological** factors?
   - **E** - How does this affect **Environmental** factors?
   - **L** - How does this affect **Legal** factors?
4. **🔗 Agent Network Effects:**
   - AI determines which agents are affected
   - Updates local PESTEL states of affected agents
5. **➡️ Continue to Next Agent/Day**

## 🚀 Quick Start

### Prerequisites
- **Java 11+**
- **Gemini API Key** (optional - system works with fallback logic)

### Installation & Setup

1. **Clone the Repository:**
```bash
git clone https://github.com/Yegmina/PESTEL_java_LLM_game_theory.git
cd PESTEL_java_LLM_game_theory
```

2. **Compile the Project:**
```bash
# Compile framework
javac -cp . -d . simu/framework/*.java

# Compile PESTEL models
javac -cp . -d . simu/model/*.java

# Compile tests
javac -cp . -d . test/*.java
```

3. **Run Basic Simulation (No AI Required):**
```bash
java -cp . test.PESTELSimulationTest
```

4. **Run with AI Integration:**
```bash
java -DGEMINI_API_KEY="your_api_key_here" -cp . test.PESTELSimulationTest
```

## 🎮 Usage Examples

### Basic PESTEL Simulation
```java
// Create simulation: 3 companies, 2 countries, 2 researchers, 7 days
PESTELSimulationEngine engine = new PESTELSimulationEngine(3, 2, 2, 7);
engine.setSimulationTime(7.0);
engine.run();
```

### Extended Strategic Planning
```java
// Long-term simulation: 30 days with more agents
PESTELSimulationEngine engine = new PESTELSimulationEngine(5, 4, 4, 30);
engine.setSimulationTime(30.0);
engine.run();
```

### War Scenario Analysis
```java
// Specialized conflict scenario testing
java -cp . test.GlobalWarPredictionTest
```

## 📊 Sample Output

```
========== DAY 1 ==========

--- Processing Company_1 ---
Company_1 decides: Launch new product line based on market research

PESTEL Change: Day 1: Company_1 changed economic.growth from 'GDP growth 2.5% annually' 
to 'Increased economic activity due to new product launch' (Reason: Innovation stimulates market growth)

Updated Country_1, Researcher_1 with PESTEL changes

--- Processing Country_1 ---
Country_1 decides: Implement innovation support policies for startups

PESTEL Change: Day 1: Country_1 changed political.policies_laws from 'Standard business policies' 
to 'Enhanced startup support framework with tax incentives' (Reason: Policy reform to boost innovation)
```

## 🎯 Strategic Applications

### For Metropolia University:
- **📈 Strategic Planning** - Long-term institutional development
- **🔍 Risk Assessment** - Identify potential challenges and opportunities  
- **🤝 Stakeholder Analysis** - Understand complex stakeholder interactions
- **📋 Policy Impact** - Evaluate effects of policy changes
- **🌍 Scenario Planning** - Prepare for multiple future possibilities

### Research Applications:
- **🎓 Academic Research** - Multi-agent systems and AI decision making
- **📊 PESTEL Methodology** - Advanced strategic analysis techniques
- **🤖 LLM Integration** - Practical AI applications in planning
- **🎮 Game Theory** - Strategic interaction modeling

## 🔧 Configuration

### Environment Variables:
```bash
# Optional: Gemini API key for AI features
export GEMINI_API_KEY="your_gemini_api_key"

# Optional: Adjust trace levels
# INFO, WAR, ERR
```

### Simulation Parameters:
- **Agent Counts** - Customize number of companies, countries, researchers
- **Simulation Duration** - Days to simulate (1-365+)
- **AI Integration** - Enable/disable AI-driven decisions
- **PESTEL Categories** - Focus on specific factors

## 🧪 Testing

```bash
# Run all tests
java -cp . test.PESTELSimulationTest

# Test conflict scenarios  
java -cp . test.GlobalWarPredictionTest

# Quick demo
java -cp . test.SimpleForecastingDemo
```

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **Metropolia University of Applied Sciences** - Project sponsorship and requirements
- **Google DeepMind** - Gemini API and Gemma 3 model access
- **Academic Community** - PESTEL analysis methodology and multi-agent systems research

## 📞 Contact

**Project Maintainer:** Yegmina  
**Repository:** [https://github.com/Yegmina/PESTEL_java_LLM_game_theory](https://github.com/Yegmina/PESTEL_java_LLM_game_theory)

---

⭐ **Star this repository if it helps your strategic planning research!** ⭐