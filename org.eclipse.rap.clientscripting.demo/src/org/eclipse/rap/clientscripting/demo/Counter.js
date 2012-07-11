var count; // TODO [tb] : could enforce js declaration for definition by server...
var preFix;

var handleEvent = function( event ) { 
  var widget = event.widget;
  count++;
  widget.setText( preFix + count );
};
