var handleEvent = function( event ) {
  var regexp = /.*[a-z].*/;
  if( event.text.match( regexp ) !== null ) {
    event.doit = false;
  }
};
