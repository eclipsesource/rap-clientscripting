var handleEvent = function( event ) {

  // TODO [tb] : does not work with backspace?
  var regexp = /^[0-9]*$/;
  var text = event.widget.getText();
  if( text.match( regexp ) === null ) {
  	event.widget.setBackground( [ 255, 0, 0 ] );
  	event.widget.setToolTip( "Digits only!" );
  } else {
  	event.widget.setBackground( null );
  	event.widget.setToolTip( null );
  }

};
