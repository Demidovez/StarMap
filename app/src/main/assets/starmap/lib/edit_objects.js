const showGraticule = (isShowStr) => {
  const opacity = isShowStr == "true" ? cnf.graticule.style.opacity : 0;

  d3.select(".graticule").style("opacity", opacity);

  saveSvg();
};

const widthGraticule = (widthStr) => {
  d3.select(".graticule").style("stroke-width", parseFloat(widthStr));

  saveSvg();
};

const colorGraticule = (color) => {
  d3.select(".graticule").style("stroke", color);

  saveSvg();
};

const opacityGraticule = (opacityStr) => {
  d3.select(".graticule").style("opacity", parseFloat(opacityStr));

  saveSvg();
};

const typeGraticule = (idType) => {
  if (parseInt(idType) == 1) {
    d3.select(".graticule").style("stroke-dasharray", null);
  } else {
    d3.select(".graticule").style("stroke-dasharray", "10, 20");
  }

  saveSvg();
};

const showMilkyWay = (isShowStr) => {
  const opacity = isShowStr == "true" ? cnf.mw.style.opacity : 0;

  d3.selectAll(".mway").style("opacity", opacity);

  saveSvg();
};

const colorStars = (color) => {
  d3.selectAll(".star").style("fill", color);

  saveSvg();
};

const opacityStars = (opacityStr) => {
  d3.selectAll(".star").style("opacity", parseFloat(opacityStr));

  saveSvg();
};

const sizeStars = (sizeStr) => {
  d3.selectAll(".star").attr("r", (d) => starSize(parseFloat(sizeStr), d));
  d3.selectAll(".planets").attr("r", (d) => planetSize(parseFloat(sizeStr), d));

  saveSvg();
};

const showConstellations = (isShowStr) => {
  const opacityLines =
    isShowStr == "true" ? cnf.constellations.style.opacity : 0;
  const opacityNames =
    isShowStr == "true" ? cnf.constellations.nameStyle.opacity : 0;

  d3.selectAll(".lines").style("opacity", opacityLines);
  d3.selectAll(".constellationnames").style("opacity", opacityNames);

  saveSvg();
};

const colorConstellations = (color) => {
  d3.selectAll(".lines").style("stroke", color);

  saveSvg();
};

const opacityConstellations = (opacityStr) => {
  d3.selectAll(".lines").style("opacity", parseFloat(opacityStr));

  saveSvg();
};

const widthConstellations = (widthStr) => {
  d3.selectAll(".lines").style("stroke-width", parseFloat(widthStr));

  saveSvg();
};

const showNames = (isShowStr) => {
  const opacityStarNames =
    isShowStr == "true" ? cnf.stars.propernameStyle.opacity : 0;
  const opacityDsoNames = isShowStr == "true" ? cnf.dsos.nameStyle.opacity : 0;
  const opacityPlanetsNames =
    isShowStr == "true" ? cnf.planets.nameStyle.opacity : 0;
  const opacityConstNames =
    isShowStr == "true" ? cnf.constellations.nameStyle.opacity : 0;

  d3.selectAll(".starNames").style("opacity", opacityStarNames);
  d3.selectAll(".dsoNames").style("opacity", opacityDsoNames);
  d3.selectAll(".planetNames").style("opacity", opacityPlanetsNames);
  d3.selectAll(".constellationnames").style("opacity", opacityConstNames);

  saveSvg();
};

const sizeNames = (sizeStr) => {
  d3.selectAll(".starNames").style("font", `${sizeStr}px ${cnf.font}`);
  d3.selectAll(".dsoNames").style("font", `${sizeStr}px ${cnf.font}`);
  d3.selectAll(".planetNames").style("font", `${sizeStr}px ${cnf.font}`);
  d3.selectAll(".constellationnames").style("font", `${sizeStr}px ${cnf.font}`);

  saveSvg();
};

const colorNames = (color) => {
  d3.selectAll(".starNames").style("fill", color);
  d3.selectAll(".dsoNames").style("fill", color);
  d3.selectAll(".planetNames").style("fill", color);
  d3.selectAll(".constellationnames").style("fill", color);

  saveSvg();
};

const langNames = (lang) => {
  d3.selectAll(".starNames").text(function (d) {
    return starPropername(lang, d.id);
  });
  d3.selectAll(".dsoNames").text(function (d) {
    return starDsoname(lang, d.id);
  });
  d3.selectAll(".planetNames").text(function (d) {
    return d.properties[lang] || "";
  });
  d3.selectAll(".constellationnames").text(function (d) {
    return d.properties[lang] || "";
  });

  saveSvg();
};
