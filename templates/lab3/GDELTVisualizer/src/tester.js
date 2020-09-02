const names = [ "Donald Trump", "United States", "Brexit", "Theresa May",
  "European Union", "Donald Tusk", "Dorus Leliveld",
  "Angela Merkel", "Mark Rutte", "Vladimir Putin" ];

const counts = Array.from(Array(names.length), () => 0)

export const sendUpdate = () => {
  const ev = new CustomEvent("testUpdate", { detail:
    {
      name: names[randInt(0, names.length)],
      increment: randInt(1, 4),
    }
  })

  document.dispatchEvent(ev)
}

function randInt(min, max) {
  // Sample a random integer from [min, max)
  min = Math.ceil(min);
  max = Math.floor(max);
  return Math.floor(Math.random() * (max - min)) + min;
}
