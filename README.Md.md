# Computer Vision Project – Image Segmentation using K-Means

## 1. Project Title
**Image Segmentation using K-Means Clustering**

## 2. Objective
The objective of this project is to divide an image into meaningful regions using the K-Means clustering algorithm. Each pixel is assigned to one of K clusters according to its colour values.

## 3. Technologies Used
- Python
- OpenCV
- NumPy
- K-Means Clustering

## 4. Project Structure

```text
computer_vision_image_segmentation_project/
│
├── main.py
├── requirements.txt
├── README.md
├── PROJECT_REPORT.md
├── .gitignore
├── sample.jpg
└── output/
    ├── original.jpg
    ├── segmented.jpg
    └── comparison.jpg
```

## 5. How the Project Works

1. Read the input image.
2. Convert the image pixels into a 2-D array.
3. Apply K-Means clustering using OpenCV.
4. Group pixels into K clusters.
5. Replace every pixel by the colour of its cluster centre.
6. Save the segmented image and comparison image.

## 6. Installation

Install Python 3.9 or newer.

Open the terminal inside this project folder and run:

```bash
pip install -r requirements.txt
```

## 7. Run the Project

For the included sample image:

```bash
python main.py --image sample.jpg --k 3
```

You can try different numbers of clusters:

```bash
python main.py --image sample.jpg --k 2
python main.py --image sample.jpg --k 4
python main.py --image sample.jpg --k 5
```

For your own image:

```bash
python main.py --image path/to/your_image.jpg --k 3
```

## 8. Output

The program creates an `output` folder containing:

- `original.jpg` – original image
- `segmented.jpg` – segmented image
- `comparison.jpg` – original and segmented images side by side

## 9. Algorithm

### K-Means Algorithm

**Input:** Image and number of clusters K

**Step 1:** Select K initial cluster centres.

**Step 2:** Calculate the distance between every pixel and every centre.

**Step 3:** Assign each pixel to the nearest centre.

**Step 4:** Recalculate each cluster centre.

**Step 5:** Repeat Steps 2–4 until the centres become stable or the maximum number of iterations is reached.

**Step 6:** Generate the segmented image.

## 10. Result

The input image is divided into K colour-based regions. Increasing K generally preserves more colour detail, while a smaller K produces fewer and larger regions.

## 11. Applications

- Medical image analysis
- Object detection
- Image compression
- Satellite image analysis
- Background separation
- Computer vision preprocessing

## 12. Author

**Student:** Prashant Bajiya  
**Branch:** B.Tech CSE – Artificial Intelligence & Machine Learning
