var handleEvent = function( event ) {

  var text = event.widget.getText();
  event.widget.setText( text.toUpperCase() );

};
