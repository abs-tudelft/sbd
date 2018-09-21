document.addEventListener("DOMContentLoaded", async () => {
  svg = d3.select("svg")
  height = svg.attr("height")
  g = svg.append("g");

  drawPoints = (dates) => {
    g.remove()
    g = svg.append("g")


    var col = g
      .selectAll("text")
      .data(dates)
      .enter() 


    cellWidth = xz(dates[1]) - xz(dates[0])


    for (i = 0; i < 10; i++) {
      var text = col.append("text")
      .attr("class", ".text")
      .attr("x", (d) => xz(d))
      .style("font-size", 10)
      .style('text-anchor', 'middle')
      .attr("pointer-events", "none")
      .text((d) => {
        if (dataTable[d] !== undefined) {
          return dataTable[d].map(a => a.topic)[i]
        } else {
          return ""
        }})
         .attr("y", 20 + 15 * i)

    }
      
      
        //.text.attr('textLength', cellWidth)
    d3.selectAll("g text").each(function (c2) {
        var size = this.getComputedTextLength()
        if (size > cellWidth - 5) {
          d3.select(this).attr('textLength', cellWidth - 5)
                         .attr('lengthAdjust', 'spacingAndGlyphs')
        
        }
    })
  }

  parseDate = d3.timeParse("%Y-%m-%d")
  data = await d3.json('./output.json')
  dataTable = {}
  xs = data.map(a => parseDate(a.data))
  ys = data.map(a => (a.result))
  xs.forEach((key, i) => dataTable[key] = ys[i]);

  zoomed = () => {
    xz = d3.event.transform.rescaleX(x);
    xAxisPath.call(xaxis.scale(xz))
    renderDates = xz.ticks()
    drawPoints(renderDates)
  }

  redraw = () => {
    d3.selectAll(".axis").remove()
    width = svg.node().getBoundingClientRect().width
    x = d3.scaleTime()
      .domain(d3.extent(xs))
      .range([0, width])

    xz = x
    xaxis = d3.axisBottom(xz)

    xaxis.tickFormat(d3.timeFormat("%e %b %Y"))

    drawPoints(x.ticks())
    xAxisPath = svg.append("g")
      .attr("transform", "translate(0," + String(height-40) + ")")
      .attr("class", "axis")

    xAxisPath.call(xaxis)
    zoom = d3.zoom()
      .on("zoom", zoomed)

    svg.selectAll("rect").remove()

    zoomRect = svg.append("rect")
      .attr("width", width)
      .attr("height", height)
      .attr("fill", "none")
      .attr("pointer-events", "all")
      .call(zoom)
  }
  redraw()
  window.addEventListener("resize", redraw);

})
