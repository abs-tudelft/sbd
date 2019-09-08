INPUT := $(wildcard *.text)


PANDOC=pandoc

PANDOC_OPTIONS=--standalone --highlight-style=pygments --toc -f markdown -F pandoc-crossref
PANDOC_OPTIONS_LATEX=--pdf-engine=xelatex --variable papersize=a4 --variable documentclass=memoir \
		     --variable classoption=article --variable mainfont="TeX Gyre Pagella" \
		     --variable mathfont="TeX Gyre Pagella Math" --variable monofont="Source Code Pro" \
		     --metadata date="`date -u '+%Y-%m-%d'`" \
		     -H preamble.tex --default-image-extension=pdf

PANDOC_OPTIONS_MD= -t gfm --wrap=preserve --default-image-extension=png

EXPORTED_DOCS=manual.md manual.pdf

all : $(EXPORTED_DOCS)

manual.pdf : $(INPUT)
	$(PANDOC) $(PANDOC_OPTIONS) $(PANDOC_OPTIONS_LATEX) -o $@ $(INPUT)

manual.tex : $(INPUT)
	$(PANDOC) $(PANDOC_OPTIONS) $(PANDOC_OPTIONS_LATEX) -o $@ $(INPUT)

manual.md : $(INPUT)
	echo $(INPUT)
	$(PANDOC) $(PANDOC_OPTIONS) $(PANDOC_OPTIONS_MD) -o $@ $(INPUT)

.PHONY: all clean


clean:
	rm -f $(EXPORTED_DOCS)
