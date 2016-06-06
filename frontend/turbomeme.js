//
// VARIABLES
//

var imgDataMap = {};

var selected = null;
var canvas = null;
var image = null;

var topTextElement = null;
var bottomTextElement = null;

var grayscale = false;
var grayScaleFilter = new fabric.Image.filters.Grayscale();
var invert = false;
var invertFilter = new fabric.Image.filters.Invert();
var sepia = false;
var sepiaFilter = new fabric.Image.filters.Sepia();
var sepia2 = false;
var sepia2Filter = new fabric.Image.filters.Sepia2();
var emboss = false;
var removeWhite = false;
var removeWhiteThreshold = 0;
var removeWhiteDistance = 0;
var brightness = false;
var brightnessValue = 0;
var noise = false;
var noiseValue = 0;
var gradientTransparency = false;
var gradientTransparencyValue = 0;
var pixelate = false;
var pixelateValue = 0;
var tint = false;
var tintColor = "";
var tintOpacityValue = 0;
var multiply = false;
var multiplyColor = "";
var blend = false;
var blendMode = "";
var blendColor = "";

//
// FUNCTIONS
//

function selectImage(event) {
  var canvas = document.getElementById("image-sheet");
  var rect = canvas.getBoundingClientRect();
  var x = event.clientX - rect.left;
  var y = event.clientY - rect.top;
  var column = Math.floor(x / 100);
  var row = Math.floor(y / 100);
  var rowArray = imageMap[row];
  loadImage(rowArray[column]);
}

function loadImage(id) {

  if (selected == null) {
    show();
  }

  // don't load the same picture again
  if (selected != null && selected == id) {
    return;
  }

  var imgData = imgDataMap[id];
  var imageCanvasElement = document.getElementById("image-canvas");
  imageCanvasElement.innerHTML = '';
  imageCanvasElement.style.width = imgData.width + "px";
  var canvasElement = document.createElement('canvas');
  canvasElement.id = 'c';
  document.body.appendChild(canvasElement);
  imageCanvasElement.appendChild(canvasElement);

  canvas = new fabric.Canvas('c', {
    width: imgData.width,
    height: imgData.height
  });

  fabric.Image.fromURL(imgData.wwwPath, function (oImg) {
    oImg.selectable = false;
    oImg.hasControls = false;
    canvas.add(oImg);
    canvas.setActiveObject(oImg);
    setImage(oImg);
    document.getElementById("image-canvas-and-filters").style.height =
      oImg.getHeight() + 20 + "px";
    reset();
    location.hash = "#section2";
  });

  selected = id;
}

function setImage(img) {
  image = img;
}

function applyGrayscale() {
  grayscale = !grayscale;
  applyFilters();
}

function applyInvert() {
  invert = !invert;
  applyFilters();
}

function applySepia() {
  sepia = !sepia;
  applyFilters();
}

function applySepia2() {
  sepia2 = !sepia2;
  applyFilters();
}

function applyRemoveWhite() {
  removeWhite = !removeWhite;
  applyFilters();
}

function updateRemoveWhiteThreshold() {
  var element = document.getElementById("remove-white-threshold");
  removeWhiteThreshold = element.value;
  applyFilters();
}

function updateRemoveWhiteDistance() {
  var element = document.getElementById("remove-white-distance");
  removeWhiteDistance = element.value;
  applyFilters();
}

function applyBrightness() {
  brightness = !brightness;
  applyFilters();
}

function updateBrightnessValue() {
  var element = document.getElementById("brightness-value");
  brightnessValue = parseInt(element.value, 10);
  applyFilters();
}

function applyNoise() {
  noise = !noise;
  applyFilters();
}

function updateNoiseValue() {
  var element = document.getElementById("noise-value");
  noiseValue = parseInt(element.value, 10);
  applyFilters();
}

function applyGradientTransparency() {
  gradientTransparency = !gradientTransparency;
  applyFilters();
}

function updateGradientTransparencyValue() {
  var element = document.getElementById("gradient-transparency-value");
  gradientTransparencyValue = parseInt(element.value, 10);
  applyFilters();
}

function applyPixelate() {
  pixelate = !pixelate;
  applyFilters();
}

function updatePixelateValue() {
  var element = document.getElementById("pixelate-value");
  pixelateValue = parseInt(element.value, 10);
  applyFilters();
}

function applyEmboss() {
  emboss = !emboss;
  applyFilters();
}

function applyTint() {
  tint = !tint;
  applyFilters();
}

function updateTintColor() {
  tintColor = document.getElementById('tint-color').value;
  applyFilters();
}

function updateTintOpacity() {
  tintOpacityValue = parseFloat(document.getElementById('tint-opacity').value);
  applyFilters();
}

function applyMultiply() {
  multiply = !multiply;
  applyFilters();
}

function updateMultiplyColor() {
  multiplyColor = document.getElementById('multiply-color').value;
  applyFilters();
}

function applyBlend() {
  blend = !blend;
  applyFilters();
}

function updateBlendMode() {
  var element = document.getElementById('blend-mode')
  blendMode = element.options[element.selectedIndex].value;
  applyFilters();
}

function updateBlendColor() {
  blendColor = document.getElementById('blend-color').value;
  applyFilters();
}

function updateFilterValues() {
  updateRemoveWhiteThreshold();
  updateRemoveWhiteDistance();
  updateBrightnessValue();
  updateNoiseValue();
  updateGradientTransparencyValue();
  updatePixelateValue();
  updateTintColor();
  updateTintOpacity();
  updateMultiplyColor();
  updateBlendColor();
}

function applyTopText() {

  if (canvas == null) {
    return;
  }

  var domTopTextElement = document.getElementById('top-text');
  var wrappedText = wrapText(domTopTextElement.value);

  if (topTextElement == null) {
    var style = generateStyle(wrappedText, true);
    topTextElement = new fabric.Text(wrappedText, style);
    canvas.add(topTextElement);
  }
  else {
    topTextElement.text = wrappedText;
    topTextElement.setCoords();
    canvas.renderAll();
  }
}

function applyBottomText() {

  if (canvas == null) {
    return;
  }

  var domBottomTextElement = document.getElementById('bottom-text');
  var wrappedText = wrapText(domBottomTextElement.value);

  if (bottomTextElement == null) {
    var style = generateStyle(wrappedText, false);
    bottomTextElement = new fabric.Text(wrappedText, style);
    canvas.add(bottomTextElement);
  }
  else {
    var lines = wrappedText.split("\n");
    bottomTextElement.top = canvas.getHeight() - (60 * lines.length);
    bottomTextElement.text = wrappedText;
    bottomTextElement.setCoords();
    canvas.renderAll();
  }
}

function wrapText(text) {
  return text;
}

function generateStyle(text, alignTop) {
  var topOffset;

  if (alignTop) {
    topOffset = 20;
  }
  else {
    var lines = text.split("\n");
    topOffset = canvas.getHeight() - (60 * lines.length);
  }

  var style = {
    fontFamily: 'Impact',
    fontSize: 50,
    fontWeight: 'bold',
    fill: "#FFFFFF",
    textAlign: 'center',
    stroke: '#000000',
    strokeWidth: 2,
    top: topOffset,
    left: 50
  };

  return style;
}

function applyFilters() {

  if (canvas == null) {
    return;
  }

  var obj = image;

  if (obj == null) {
    return;
  }

  obj.filters = [];

  if (grayscale) {
    obj.filters.push(grayScaleFilter);
  }

  if (invert) {
    obj.filters.push(invertFilter);
  }

  if (sepia) {
    obj.filters.push(sepiaFilter);
  }

  if (sepia2) {
    obj.filters.push(sepia2Filter);
  }

  if (removeWhite) {
    var filter = new fabric.Image.filters.RemoveWhite({
      threshold: removeWhiteThreshold,
      distance: removeWhiteDistance
    });
    obj.filters.push(filter);
  }

  if (brightness) {
    var filter = new fabric.Image.filters.Brightness({
      brightness: brightnessValue
    });
    obj.filters.push(filter);
  }

  if (noise) {
    var filter = new fabric.Image.filters.Noise({
      noise: noiseValue
    });
    obj.filters.push(filter);
  }

  if (gradientTransparency) {
    var filter = new fabric.Image.filters.GradientTransparency({
      threshold: gradientTransparencyValue
    });
    obj.filters.push(filter);
  }

  if (pixelate) {
    var filter = new fabric.Image.filters.Pixelate({
      blocksize: pixelateValue
    });
    obj.filters.push(filter);
  }

  if (emboss) {
    var filter = new fabric.Image.filters.Convolute({
      matrix: [ 1, 1, 1,
        1, 0.7, -1,
        -1, -1, -1 ]
    });
    obj.filters.push(filter);
  }

  if (tint) {
    var filter = new fabric.Image.filters.Tint({
      color: tintColor,
      opacity: tintOpacityValue
    });
    obj.filters.push(filter);
  }

  if (multiply) {
    var filter = new fabric.Image.filters.Multiply({
      color: multiplyColor
    })
    obj.filters.push(filter);
  }

  if (blend) {
    var filter = new fabric.Image.filters.Blend({
      color: blendColor,
      mode: blendMode
    });
    obj.filters.push(filter);
  }

  obj.applyFilters(canvas.renderAll.bind(canvas));
}

function publish() {
  var jsonInput = document.getElementById("json");
  var json = JSON.stringify(canvas);
  jsonInput.value = json;

  var memeImageIdInput = document.getElementById("meme-image-id");
  memeImageIdInput.value = selected;
}

function loadImageData() {
  for (var i in imageData) {
    var id = imageData[i].id;
    imgDataMap[id] = imageData[i];
  }
}

function loadImageSheet() {
  var canvas = document.getElementById("image-sheet");
  var ctx = canvas.getContext("2d");
  var background = new Image();
  background.src = "img/ui/image-sheet.jpg";

  background.onload = function () {
    ctx.drawImage(background, 0, 0);
  };
}

function addMouseListener() {
  var canvas = document.getElementById("image-sheet");
  canvas.addEventListener("mousedown", selectImage, false);
}

function reset() {

  if (topTextElement != null) {
    canvas.remove(topTextElement);
    topTextElement = null;
  }

  if (bottomTextElement != null) {
    canvas.remove(bottomTextElement);
    bottomTextElement = null;
  }

  grayscale = false;
  invert = false;
  sepia = false;
  sepia2 = false;
  emboss = false;
  removeWhite = false;
  brightness = false;
  noise = false;
  gradientTransparency = false;
  pixelate = false;
  tint = false;
  multiply = false;
  blend = false;

  document.getElementById('top-text').value = "";
  document.getElementById('bottom-text').value = "";
  document.getElementById("grayscale").checked = false;
  document.getElementById("invert").checked = false;
  document.getElementById("sepia").checked = false;
  document.getElementById("sepia2").checked = false;
  document.getElementById("remove-white").checked = false;
  document.getElementById("brightness").checked = false;
  document.getElementById("noise").checked = false;
  document.getElementById("gradient-transparency").checked = false;
  document.getElementById("pixelate").checked = false;
  document.getElementById("emboss").checked = false;
  document.getElementById("tint").checked = false;
  document.getElementById("tint-color").value = "#00f900";
  document.getElementById("tint-opacity").value = "0.5";
  document.getElementById("multiply").checked = false;
  document.getElementById("multiply-color").value = "#00f900";
  document.getElementById("blend").checked = false;
  document.getElementById("blend-mode").selectedIndex = 0;
  document.getElementById("blend-color").value = "#00f900";
}

function show() {
  document.getElementById('section2').style.display = "block";
}

function hide() {
  document.getElementById('section2').style.display = "none";
}

//
// INITIALIZATION
//

reset();
hide();
updateFilterValues();
loadImageData();
loadImageSheet();
addMouseListener();