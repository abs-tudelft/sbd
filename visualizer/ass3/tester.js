let intervalId = undefined

const names = [ "Donald Trump", "United States", "Brexit", "Theresa May",
  "European Union", "Donald Tusk", "Dorus Leliveld",
  "Angela Merkel", "Mark Rutte", "Vladimir Putin" ];

const counts = Array.from(Array(names.length), () => 0)

const sendUpdate = () => {
  const ev = new CustomEvent("testUpdate", { detail:
    {
      name: names[randInt(0, names.length)],
      increment: randInt(1, 4),
    }
  })

  document.dispatchEvent(ev)
}
