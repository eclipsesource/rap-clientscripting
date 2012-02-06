var handleEvent = function( event ) { 
  var widget = event.widget;
  var count = widget.getData( "count" );
  if( count === null ) {
    count = 0;
  }
  count++;
  widget.setData( "count", count );
  widget.setText( "Click " + count );
};
