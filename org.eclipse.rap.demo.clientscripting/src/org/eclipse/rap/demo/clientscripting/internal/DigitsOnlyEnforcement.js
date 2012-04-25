var handleEvent = function( event ) {
  var regexp = /^[0-9]*$/;
  if( event.text.match( regexp ) === null ) {
    event.doit = false;
  }
};
