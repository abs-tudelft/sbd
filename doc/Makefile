INPUT := $(wildcard *.text)


PANDOC=pandoc

PANDOC_OPTIONS=--filter pandoc-citeproc --standalone --highlight-style=pygments --toc -f markdown -F pandoc-crossref
PANDOC_OPTIONS_LATEX=--pdf-engine=xelatex --variable papersize=a4 --variable documentclass=memoir \
		     --variable classoption=article --variable mainfont="TeX Gyre Pagella" \
		     --variable mathfont="TeX Gyre Pagella Math" --variable monofont="Source Code Pro" \
		     -H preamble.tex --default-image-extension=pdf

PANDOC_OPTIONS_MD= -t gfm --wrap=preserve --default-image-extension=png

EXPORTED_DOCS=\
 $(INPUT:.text=.md)\
 $(INPUT:.text=.pdf)\

%.pdf : %.text
	$(PANDOC) $(PANDOC_OPTIONS) $(PANDOC_OPTIONS_LATEX) -o $@ $<

%.tex : %.text
	$(PANDOC) $(PANDOC_OPTIONS) $(PANDOC_OPTIONS_LATEX) -o $@ $<

%.md : %.text
	$(PANDOC) $(PANDOC_OPTIONS) $(PANDOC_OPTIONS_MD) -o $@ $<

.PHONY: all clean

all : $(EXPORTED_DOCS)

clean:
	rm $(EXPORTED_DOCS)
