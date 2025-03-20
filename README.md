# DSA Visualization System

A comprehensive tool for visualizing data structures and algorithms to help students and developers understand complex computer science concepts through interactive animations.

![DSA Visualization System](https://placeholder-for-project-screenshot.png)

[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-21-orange)](https://www.oracle.com/java/)
[![Maven](https://img.shields.io/badge/Maven-3.8.1-purple)](https://maven.apache.org/)

## 📋 Table of Contents

- [Features](#-features)
<!-- - [Demo](#-demo) -->
- [Technologies Used](#-technologies-used)
<!-- - [Installation](#-installation) -->
- [Usage](#-usage)
- [Algorithms & Data Structures](#-algorithms--data-structures)
- [Project Structure](#-project-structure)
- [Contributing](#-contributing)
- [License](#-license)
- [Contact](#-contact)

## ✨ Features

- Interactive visualization of common data structures and algorithms
- Step-by-step execution with visual feedback
- Codes for multiple programming languages (JavaScript, C++, etc.)
- Customizable animation speed and input data
- Educational descriptions of algorithm functioning

<!-- ## 🎮 Demo -->

<!-- [View Live Demo](#) (Add your demo link here) -->

<!-- ![Algorithm Visualization](https://placeholder-for-demo-gif.gif) -->

## 🛠️ Technologies Used

- Java (JDK 21)
- JavaFX for UI components
- Maven for project management
- JSON for algorithm code storage

## 📥 Installation

### Prerequisites

- Java JDK 21 or higher
- Maven 3.6+ (for building from source)

### Build from Source

1. Clone the repository
    ```bash
    git clone https://github.com/yourusername/DSA-Visualisation-System.git
    cd DSA-Visualisation-System
    ```

2. Build with Maven
    ```bash
    mvn clean install
    ```

3. Run the application
    ```bash
    mvn javafx:run
    ```

<!-- ### Download Release -->

<!-- - [Download the latest release](https://github.com/yourusername/DSA-Visualisation-System/releases) -->
<!-- - Extract and run `java -jar DSA-Visualisation-System.jar` -->

## 📝 Usage

1. Launch the application
2. Select an algorithm or data structure from the menu
3. Input your data or use the provided examples
4. Use the control panel to:
    - Start visualization
    - Step forward/backward
    - Adjust animation speed
    - Reset the visualization

## 📊 Algorithms & Data Structures

### Searching Algorithms
- Linear Search
  - Time Complexity: O(n)
  - Space Complexity: O(1)
- Binary Search
  - Time Complexity: O(log n)
  - Space Complexity: O(1) for iterative implementation, O(log n) for recursive implementation

### Sorting Algorithms
- Bubble Sort
  - Time Complexity: O(n²) average and worst case, O(n) best case
  - Space Complexity: O(1)
  - A simple comparison-based algorithm that repeatedly steps through the list, compares adjacent elements, and swaps them if they are in the wrong order

- Selection Sort
  - Time Complexity: O(n²) in all cases
  - Space Complexity: O(1)
  - Algorithm that sorts by repeatedly finding the minimum element from the unsorted portion and putting it at the beginning

### Data Structures
- Array
  - Operations:
    - Access: O(1)
    - Search: O(n) for unsorted, O(log n) for sorted with binary search
    - Insert: O(n) (requires shifting elements)
    - Delete: O(n) (requires shifting elements)
  - A basic data structure that stores elements at contiguous memory locations, allowing direct access by index

- Stack
  - Operations:
    - Push: O(1)
    - Pop: O(1)
    - Peek: O(1)
    - Search: O(n)
  - A linear data structure following LIFO (Last-In-First-Out) principle where elements are added and removed from the same end

- Queue
  - Operations:
    - Enqueue: O(1)
    - Dequeue: O(1)
    - Peek: O(1)
    - Search: O(n)
  - A linear data structure following FIFO (First-In-First-Out) principle where elements are added at one end and removed from the other

- LinkedList
  - Operations:
    - Access: O(n)
    - Search: O(n)
    - Insert at beginning/end: O(1)
    - Insert in middle: O(n)
    - Delete at beginning: O(1)
    - Delete in middle/end: O(n)
  - A linear data structure where elements are stored in nodes containing data and reference to the next node, allowing efficient insertion and deletion

## 📁 Project Structure

```
DSA-Visualisation-System/
├── src/main/
│   ├── java/com/dsa_visualisation/
│   │   ├── *Controller.java     # UI controllers for visualizations
│   │   ├── *NotesController.java # Controllers for algorithm/DS notes
│   │   ├── NotesLoader.java     # JSON notes loader
│   │   └── Main.java            # Application entry point
│   └── resources/
│       ├── com/dsa_visualisation/
│       │   ├── Codes/           # Algorithm implementations in JSON
│       │   ├── Notes/           # Educational content in JSON
│       │   ├── fxml/            # UI layout files
│       │   ├── icons/           # System icons
│       │   ├── individualDSA/   # Code syntax highlighting
│       │   └── styles/          # CSS styles
│       └── META-INF/
├── pom.xml                      # Maven configuration
└── README.md                    # This file
```

## 👥 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📬 Contact

Project Maintainer - [andrewappah.aa@gmail.com](mailto:andrewappah.aa@gmail.com)

Project Link: [https://github.com/yourusername/DSA-Visualisation-System](https://github.com/yourusername/DSA-Visualisation-System)