var handleEvent = function( event ) {

  var regexp = /^[0-9]*$/;
  var text = event.widget.getText();
  if( text.match( regexp ) === null ) {
  	event.widget.setBackground( [ 255, 255, 128 ] );
  	event.widget.setToolTip( "Digits only!" );
  } else {
  	event.widget.setBackground( null );
  	event.widget.setToolTip( null );
  }

};
