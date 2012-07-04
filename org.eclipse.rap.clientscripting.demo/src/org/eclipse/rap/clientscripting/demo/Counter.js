var handleEvent = function( event ) { 
  var widget = event.widget;
  var count = widget.getData( "count" );
  if( count === null ) {
    count = this.initNumber;
  }
  count++;
  widget.setData( "count", count );
  widget.setText( this.preFix + count );
};
