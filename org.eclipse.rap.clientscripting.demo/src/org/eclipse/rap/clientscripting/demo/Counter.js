var handleEvent = function( event ) { 
  var widget = event.widget;
  count++;
  widget.setText( preFix + count );
};
