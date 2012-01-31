function( ev ) { 
  var widget = ev.widget;
  var lastNumber = widget.getData( "number" );
  if( lastNumber === null ) {
    lastNumber = 0;
  }
  lastNumber++;
  widget.setData( "number", lastNumber );
  widget.setText( "Click " + lastNumber );
};
