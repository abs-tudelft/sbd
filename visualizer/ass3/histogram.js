const margin = ({top: 20, right: 0, bottom: 180, left: 100})
const height = 700
const width = 1280

let ws = undefined

let enabled = false

const testButton = document.querySelector("#test")
testButton.addEventListener("click", toggle)

const websocketButton = document.querySelector("#websocket")
websocketButton.addEventListener("click", toggle)

const sortFuncs = Object.freeze({ "asc": d3.ascending, "desc": d3.descending})

let data = d3.map()

const x = d3.scaleBand()
  .range([margin.left, width - margin.right])
  .padding(0.1)

const y = d3.scaleLinear()

function toggle(event) {
  if (event.target.id === "test") {
    toggleTest()
  }

  if (event.target.id === "websocket") {
    toggleWebSocket()
  }

  if (!enabled) {
    data = d3.map()
    svg.selectAll("#bars > *").remove()
  }

  enabled = !enabled
}

function toggleTest() {

  if (enabled) {
    testButton.innerHTML = "Start test"
    clearInterval(intervalId)
  }
  else {
    testButton.innerHTML = "Stop test"
    intervalId = window.setInterval(sendUpdate, 1000)
  }
  websocketButton.disabled = !enabled
}

function toggleWebSocket() {
  if (enabled) {
    websocketButton.innerHTML = "Start websocket"
    ws.close()
  }
  else {
    websocketButton.innerHTML = "Stop websocket"
    ws = new WebSocket("ws://localhost:1337")
    ws.onmessage = (event) => {console.log(event.data); onUpdate("websocket", JSON.parse(event.data)) }
    ws.onclose = onClose
  }
  testButton.diabled = !enabled
}

function onClose() {
  websocketButton.innerHTML = "Start websocket"
  testButton.disabled = false
  enabled = false
}


function randInt(min, max) {
  // Sample a random integer from [min, max)
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min)) + min;
}

const chart = () => {
  const svg = d3.select("body").append("svg");

  svg.attr("width", width)
    .attr("height", height)

  svg.append("g")
    .attr("fill", "steelblue")
    .attr("id", "bars")

  svg.append("g")
    .attr("id", "xAxis")

  svg.append("g")
    .attr("id", "yAxis")

  return svg
}

const updateChart = () => {
  // Set up data
  const entries = data.entries()

  const order = document.querySelector(".sortOrder:checked").value
  const by = document.querySelector(".sortBy:checked").value
  entries.sort((a, b) => sortFuncs[order](a[by], b[by]))

  // Set up x-axis
  x.domain(entries.map((a) => a.key))

  const xAxis = svg.select("#xAxis")
  xAxis.transition()
    .call(d3.axisBottom(x)
      .tickSizeOuter(0)
    )

  xAxis.attr("transform", `translate(0,${height - margin.bottom})`)
    .selectAll("text")
    .attr("dx", -9)
    .attr("transform", "rotate(-45)")
    .style("text-anchor", "end");

  // Set up y-axis
  y.domain([0, d3.max(data.values())])
    .range([height - margin.bottom, margin.top])

  const yAxis = svg.select("#yAxis")
  yAxis.transition()
    .call(
      d3.axisLeft(y)
      .ticks(Math.min(10, d3.max(data.values())))
    )
    .attr("transform", `translate(${margin.left},0)`)

  yAxis.select(".domain").remove()

  // Create, update and remove bars if necessary
  const bars = svg.select("#bars")

  bars.selectAll("rect").data(entries).enter().append("rect")
    .attr("x", d => x(d.key))
    .attr("y", d => y(0))
    .attr("height", 0)
    .attr("width", x.bandwidth())

  bars.selectAll("rect")
    .transition()
    .attr("x", d => x(d.key))
    .attr("y", d => y(d.value))
    .attr("height", d => y(0) - y(d.value))
    .attr("width", x.bandwidth())

  bars.selectAll("rect").exit().remove()
}

const onUpdate = (type, payload) => {
  payload = Array.isArray(payload) ? payload : [ payload ]
  for (const update of payload) {
    let value = 0
    if (type === "websocket") {
      value = update.count
    }
    else if (type === "test") {
      value = data.get(update.name) + update.increment || update.increment
    }

    if (value === 0) {
      data.remove(update.name)
    }
    else {
      data.set(
        update.name,
        value
      )
    }
  }

  updateChart()
}

const svg = chart()
document.addEventListener("testUpdate", (event) => onUpdate("test", event.detail) )
document.querySelectorAll("input[type=radio]").forEach((el) => el.addEventListener('change', updateChart))
