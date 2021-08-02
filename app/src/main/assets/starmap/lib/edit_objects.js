const showGraticule = (isShow) => {
  const opacity = isShow ? cnf.graticule.style.opacity : 0;

  d3.select(".graticule").style("opacity", opacity);

  saveSvg();
};
