var handleEvent = function( event ) { 
  var widget = event.widget;
  this.count++;
  widget.setText( this.preFix + this.count );
};
