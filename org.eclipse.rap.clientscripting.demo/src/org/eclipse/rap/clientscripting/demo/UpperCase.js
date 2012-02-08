var handleEvent = function( event ) {
  
  if( event.keyCode === 0 && event.start === event.end ) {
    event.text = "dont paste here!";
  } else {
    event.text = event.text.toUpperCase();
  }

};
