const MARGIN = 25;
let radius = 0.1;

function paint(r = null) {

    if (r != null) {
        radius = r;
    }

    let canvas = $("#areaCanvas");
    canvas.unbind();
    let context = canvas.get(0).getContext("2d");

    let bgColor = "#FFFFFF";
    let gridColor = "#C0C0C0";
    let areaColor = "#1E90FF";
    let axisColor = "#000000";
    let possibleAreaColor = "#FFFFFF";

    let hitColor = "#0F0";
    let missColor = "#F00";

    let coords = canvas.get(0).getBoundingClientRect();
    let width = coords.right - coords.left;
    let height = coords.bottom - coords.top;

    let centerX = width / 2;
    let centerY = height / 2;

    let pointDistance = ( Math.min(width, height) - 2 * MARGIN ) / 6; // distance between two closest points on axis

    let arrowLength = pointDistance / 3;
    let arrowWidth = pointDistance / 10;

    let textSize = pointDistance / 5;
    let textMarginX = textSize / 2;
    let textMarginY = textSize;

    context.fillStyle = bgColor;
    context.fillRect(0, 0, width, height);

    canvas.click( function(e) {
        let coords = canvas.get(0).getBoundingClientRect();
        let canvasX = e.clientX - coords.left;
        let canvasY = e.clientY - coords.top;
        console.log(document.getElementById("main-form:spinner").value);

        console.log(canvasX + " " + canvasY);
        let [x, y] = fromAbsoluteToDecarteCoordinates(canvasX, canvasY, radius, centerX, centerY, pointDistance);
        console.log("Координаты: " + x + " " + y);

        document.getElementById("hiddenX").value = x;
        document.getElementById("hiddenY").value = y;
        document.getElementById("hiddenR").value = radius;
        document.getElementById("hiddenButton").click();

    } );

    // DRAWING areas
    context.fillStyle = areaColor;
    context.beginPath();
    context.moveTo(centerX, centerY);
    context.lineTo(centerX + pointDistance, centerY);
    context.lineTo(centerX, centerY + 2 * pointDistance);
    context.lineTo(centerX, centerY + pointDistance);
    context.arc(centerX, centerY, pointDistance, Math.PI / 2,Math.PI);
    context.lineTo(centerX - pointDistance, centerY - 2 * pointDistance);
    context.lineTo(centerX, centerY - 2 * pointDistance);
    context.lineTo(centerX, centerY);
    context.fill();

    // DRAWING grid
    context.strokeStyle = gridColor;
    context.beginPath();
    for (let i = 0; i < 13; i++) {
        for (let j = 0; j < 13; j++) {
            let positionX = centerX + (i - 6) * pointDistance / 2;
            context.moveTo(positionX, MARGIN);
            context.lineTo(positionX, height - MARGIN);

            let positionY = centerY + (i - 6) * pointDistance / 2;
            context.moveTo(MARGIN, positionY);
            context.lineTo(width - MARGIN, positionY);
        }
    }
    context.stroke();

    // DRAWING axis and numbers
    context.strokeStyle = axisColor;
    context.fillStyle = axisColor;
    context.font = textSize + "px monospace";
    context.beginPath();

    // X axis
    context.moveTo(MARGIN, centerY);
    context.lineTo(width - MARGIN, centerY);
    context.lineTo(width - MARGIN - arrowLength, centerY - arrowWidth);
    context.moveTo(width - MARGIN, centerY);
    context.lineTo(width - MARGIN - arrowLength, centerY + arrowWidth);
    context.fillText("X", width - MARGIN + textMarginX, centerY);

    // Y axis
    context.moveTo(centerX, height - MARGIN);
    context.lineTo(centerX, MARGIN);
    context.lineTo(centerX - arrowWidth, MARGIN + arrowLength);
    context.moveTo(centerX, MARGIN);
    context.lineTo(centerX + arrowWidth, MARGIN + arrowLength);
    context.fillText("Y", centerX + textMarginX, MARGIN);

    // points and texts on both axis
    let text = [-radius, -radius / 2, 0, +radius / 2, +radius];

    for (let i = 0; i < 5; i++) {
        let positionX = centerX + pointDistance * (i - 2);
        context.moveTo(positionX, centerY - arrowWidth);
        context.lineTo(positionX, centerY + arrowWidth);
        context.fillText(text[i], positionX + textMarginX, centerY + textMarginY);

        let positionY = centerY + pointDistance * (i - 2);
        context.moveTo(centerX - arrowWidth, positionY);
        context.lineTo(centerX + arrowWidth, positionY);
        context.fillText(text[4 - i], centerX + textMarginX, positionY + textMarginY);
    }

    context.stroke();

    // DRAWING possible areas for x and y
    let [left, bottom] = fromDecarteToAbsoluteCoordinates(-2, -3, radius, centerX, centerY, pointDistance);
    let [right, top] = fromDecarteToAbsoluteCoordinates(2, 3, radius, centerX, centerY, pointDistance);

    if (left == null) left = MARGIN;
    if (top == null) top = MARGIN;
    if (right == null) right = width - MARGIN;
    if (bottom == null) bottom = height - MARGIN;


    context.globalAlpha = 1;


    // DRAWING history points
    if (radius == null) return;
    let historyX = [];
    $(".history-x").each(function() {
        historyX.push( +$(this).text().trim().substring(0, 15) );
    });

    let historyY = [];
    $(".history-y").each(function() {
        historyY.push( +$(this).text().trim().substring(0, 15) );
    });

    for (let i = 0; i < historyX.length; i++) {
        let [dotX, dotY] = fromDecarteToAbsoluteCoordinates(historyX[i], historyY[i], radius, centerX, centerY, pointDistance);
        if (dotX == null || dotY == null) continue;

        context.beginPath();
        context.moveTo(dotX, dotY);
        context.arc(dotX, dotY, 4, 0, Math.PI * 2);

        context.fillStyle = calculateHit(historyX[i], historyY[i], radius) ? hitColor : missColor;
        context.fill();
    }

}

function calculateHit(x, y, r) {
    return (x <= 0 && x >= -r / 2 && y <= r && y >= 0) ||
           (x <= 0 && y <= 0 && x * x + y * y <= r * r / 4) ||
           (x >= 0 && y <= 0 && y >= 2 * x - r);
}

function fromDecarteToAbsoluteCoordinates(x, y, r, centerX, centerY, pointDistance) {
    let ratioX = x / r * 2;
    let ratioY = y / r * 2;

    let canvasX, canvasY;
    if (ratioX < -3 || ratioX > 3) canvasX = null;
    else canvasX = centerX + pointDistance * ratioX;

    if (ratioY < -3 || ratioY > 3) canvasY = null;
    else canvasY = centerY - pointDistance * ratioY;

    return [canvasX, canvasY];
}

function fromAbsoluteToDecarteCoordinates(canvasX, canvasY, r, centerX, centerY, pointDistance) {
    let ratioX = ( canvasX - centerX ) / pointDistance;
    let ratioY = ( centerY - canvasY ) / pointDistance;

    let x, y;
    if (ratioX < -3 || ratioX > 3) x = null;
    else x = ratioX / 2 * r;

    if (ratioY < -3 || ratioY > 3) y = null;
    else y = ratioY / 2 * r;

    return [x, y];
}

$(document).ready(function(){
    paint();
});