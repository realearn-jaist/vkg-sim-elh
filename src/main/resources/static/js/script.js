// Function to update the upload status
function updateUploadStatus(fileInputId, statusElementId) {
  const fileInput = document.getElementById(fileInputId);
  const statusElement = document.getElementById(statusElementId);

  // Check if a file is selected
  if (fileInput.files.length > 0) {
    // File is uploaded
    statusElement.innerHTML =
      '<span class="text-success">&#10004; Uploaded</span>';
  } else {
    // No file is uploaded
    statusElement.innerHTML =
      '<span class="text-danger">&#10008; Not Uploaded</span>';
  }
}

// Add event listeners to file input elements
document.getElementById("mappingFile").addEventListener("change", function () {
  updateUploadStatus("mappingFile", "mappingFileStatus");
});

document
  .getElementById("similarityFile")
  .addEventListener("change", function () {
    updateUploadStatus("similarityFile", "similarityFileStatus");
  });

document.getElementById("mappingFile").addEventListener("change", function () {
  const fileInput = this;
  const textArea = document.getElementById("mappingTextArea");

  if (fileInput.files.length > 0) {
    const selectedFile = fileInput.files[0];
    const reader = new FileReader();

    reader.onload = function (e) {
      const fileContent = e.target.result;
      textArea.value = fileContent;
    };

    reader.readAsText(selectedFile);
  } else {
    // Handle the case where no file is selected or the selection is cleared
    textArea.value = ""; // Clear the textarea
  }
});

function downloadText() {
    const textArea = document.getElementById('resultMapping');
    const textToDownload = textArea.value;

    // Create a Blob containing the text
    const blob = new Blob([textToDownload], { type: 'text/plain' });

    // Create a URL for the Blob
    const url = window.URL.createObjectURL(blob);

    // Create a download link
    const a = document.createElement('a');
    a.href = url;
    a.download = 'newMapping.obda'; // You can set the filename here
    a.style.display = 'none';

    // Add the link to the document and trigger a click event
    document.body.appendChild(a);
    a.click();

    // Clean up
    window.URL.revokeObjectURL(url);
    document.body.removeChild(a);
}
