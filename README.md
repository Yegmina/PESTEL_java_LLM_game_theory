# 🚀 PESTEL Simulation with 3D Vector Visualization

## 📊 **COMPLETE SYSTEM OVERVIEW**

This project combines **AI-powered PESTEL analysis** with **impressive 3D vector visualization** to simulate and predict alternative futures for strategic planning.

### **🎯 What This System Does:**
- **Simulates 190 real-world entities** (100 companies, 50 countries, 40 research centers)
- **Uses AI for decision-making** (with database logging and fallback logic)
- **Creates 1500+ dimensional vector embeddings** for each alternative future
- **Visualizes temporal evolution** in interactive 3D space
- **Predicts dominant futures** with narrative bias and probability tracking

---

## 🏗️ **PROJECT STRUCTURE**

```
oracle_java/
├── simu/                    # Core simulation engine
│   ├── framework/          # Base simulation framework
│   └── model/              # PESTEL models and AI services
├── test/                   # Simulation tests
├── DataBase/               # Database integration (MariaDB)
└── PestelViz/              # 🆕 3D Vector Visualization UI
    ├── src/app/
    │   ├── Main.java       # JavaFX application entry
    │   ├── controller/     # UI controllers
    │   └── model/          # 3D vector models
    ├── resources/          # FXML and CSS
    └── compile.bat         # Build script
```

---

## 🚀 **HOW TO RUN**

### **Prerequisites:**
- **Java 17+** (required for JavaFX text blocks)
- **JavaFX SDK 25** installed at `C:\javafx\javafx-sdk-25`
- **MariaDB** (optional, for database logging)

### **Option 1: 3D Vector Visualization (RECOMMENDED)**

```powershell
# Navigate to the visualization module
cd PestelViz

# Compile the JavaFX application
.\compile.bat

# Run the impressive 3D visualization
.\run.bat
```

**What you'll see:**
- **Interactive 3D canvas** showing vector evolution
- **Drag to rotate** the 3D view
- **Scroll to zoom** in/out
- **Real-time updates** as simulation progresses
- **Dynamic probabilities** with narrative bias
- **Timeframe controls** to view specific periods

### **Option 2: Database Test**

```powershell
# Navigate to database module
cd DataBase

# Set your Metropolia password
$env:DB_PASSWORD = "your_metropolia_password"

# Compile and run database test
javac -cp "lib\mariadb-java-client-3.5.6.jar;src" -d out src\entity\*.java src\datasource\*.java src\dao\*.java src\test\*.java
java -cp "out;lib\mariadb-java-client-3.5.6.jar" test.TestDatabase
```

### **Option 3: Console Simulation**

```powershell
# From project root
javac -cp . simu/framework/*.java simu/model/*.java test/*.java
java -cp . test.FixedRealWorldTest
```

---

## 🎨 **3D VISUALIZATION FEATURES**

### **🌟 Core Features:**
- **1500+ Dimensional Vectors**: Each future scenario represented in high-dimensional space
- **Temporal Progression**: Day0→Day1→Day2... vector chains showing evolution
- **3D Compression**: Intelligent compression to 3D for visualization
- **Interactive Controls**: Rotate, zoom, timeframe selection
- **Real-time Updates**: Live data from simulation engine

### **🎯 Visual Elements:**
- **Flowing Vector Paths**: Connected lines showing temporal evolution
- **Glowing Spheres**: Current positions with probability-based sizing
- **Color-coded Scenarios**: Each future has distinct colors
- **3D Coordinate System**: Tech/Econ, Soc/Env, Political axes
- **Rich Metrics**: Vector distance, active scenarios, dominant future

### **🎮 Interactive Controls:**
- **Mouse Drag**: Rotate the 3D view
- **Mouse Wheel**: Zoom in/out
- **Timeframe Fields**: Select specific day ranges
- **Update View Button**: Refresh with new timeframe

---

## 🤖 **AI INTEGRATION**

### **AI Services Available:**
1. **Database Mode**: Uses real AI responses stored in MariaDB
2. **Random Mode**: Uses intelligent fallback logic
3. **Ollama Mode**: Local AI via Ollama (if available)

### **Narrative Bias:**
- **Tech Futures** (AI Supremacy, Quantum Revolution) get probability boosts
- **Green Futures** (Green Transition, Climate Response) get probability boosts
- **Other futures** gradually decrease in likelihood
- **Creates clear "prediction"** showing which futures are becoming dominant

---

## 📈 **SIMULATION PARAMETERS**

- **Duration**: 30 days (configurable)
- **Entities**: 190 total (100 companies + 50 countries + 40 research centers)
- **Alternative Futures**: 12 scenarios with dynamic probabilities
- **PESTEL Factors**: 60+ variables across 6 categories
- **Cross-agent Effects**: Realistic influence networks

---

## 🔧 **TROUBLESHOOTING**

### **Common Issues:**

1. **"Day 0" Problem**: 
   - **Solution**: The new 3D system fixes this - days will progress properly

2. **JavaFX Not Found**:
   - **Solution**: Install JavaFX SDK 25 at `C:\javafx\javafx-sdk-25`

3. **Database Connection**:
   - **Solution**: Set `$env:DB_PASSWORD = "your_password"` or use Random Mode

4. **Compilation Errors**:
   - **Solution**: Ensure Java 17+ is installed for text block support

### **Performance Tips:**
- Use **Random Mode** for faster simulation
- Adjust **Simulation Speed** slider for desired pace
- Use **Timeframe Selection** to focus on specific periods

---

## 🎯 **WHAT MAKES THIS IMPRESSIVE**

1. **High-Dimensional Thinking**: 1500+ dimensions compressed to 3D
2. **Temporal Evolution**: Shows how futures change over time
3. **Interactive 3D**: Full rotation and zoom control
4. **Real-time Dynamics**: Live updates as simulation progresses
5. **Narrative Intelligence**: AI-driven bias toward realistic outcomes
6. **Professional UI**: Modern dark theme with smooth animations
7. **Rich Metrics**: Comprehensive data visualization

---

## 🚀 **QUICK START**

```powershell
# 1. Navigate to visualization
cd PestelViz

# 2. Compile
.\compile.bat

# 3. Run
.\run.bat

# 4. Click "Start" and watch the 3D magic happen!
```

**The system is now ready for impressive, interactive 3D vector visualization of alternative futures!** 🎉