//
// FUNCTIONS
//

function loadImage() {
  var canvasWidthElement = document.getElementById("canvas-width");
  var canvasHeightElement = document.getElementById("canvas-height");
  var canvas = new fabric.StaticCanvas('c', {
    width: canvasWidthElement.value,
    height: canvasHeightElement.value
  });

  var inputElement = document.getElementById("json");
  var json = inputElement.value;
  canvas.loadFromJSON(json, canvas.renderAll.bind(canvas), function (o, object) {
    if (object.type === 'image' && object.filters.length) {
      object.applyFilters(function () {
        object.canvas.renderAll();
      });
    }
  });
}

//
// INITIALIZATION
//

loadImage();