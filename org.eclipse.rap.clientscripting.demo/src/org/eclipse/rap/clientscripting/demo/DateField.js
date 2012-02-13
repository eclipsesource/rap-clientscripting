var handleEvent = function( event ) { 
  switch( event.type ) {
    case SWT.KeyDown:
      handleKeyDownEvent( event );
    break;
    case SWT.MouseDown:
    case SWT.MouseUp:
      handleMouseEvent( event );
    break;
    case SWT.Verify:
      handleVerifyEvent( event );
    break;
  }
};

var handleKeyDownEvent = function( event ) {
  var newCh = event.character;
  var keyCode = event.keyCode;
  var sel = event.widget.getSelection()[ 0 ];
  var text = event.widget.getText();

  var replaceNextChar = function( value ) {
    var leftPart = text.slice( 0, sel );
    var rightPart = text.slice( sel + 1, text.length );
    text = leftPart + value + rightPart;
    sel++;
  };

  var replacePrevChar = function( value ) {
    var leftPart = text.slice( 0, sel - 1 );
    var rightPart = text.slice( sel, text.length );
    text = leftPart + value + rightPart;
    sel--;
  };
  
  if( text.charAt( sel ) === "_" && isNumber( newCh ) ) {
    replaceNextChar( newCh );
    if( text.charAt( sel ) === "." ) {
      sel++;
    }    
  } else if( keyCode === SWT.BS && sel > 0 ) {
    if( text.charAt( sel - 1 ) === "." ) {
      sel--;
    }
    if( isNumber( text.charAt( sel - 1 ) ) ) {
      replacePrevChar( "_" );
    } else {
      sel--;
    }
    event.doit = false;
  }

  // TODO: Setting text also sets selection to last position? - compare to SWT
  event.widget.setText( text );
  event.widget.setSelection( [ sel, sel ] );
  event.doit = false; // prevent key input 
}

var handleMouseEvent = function( event ) {
  // not supported: 
  //event.doit = false; // prevent mouse selection change
}

var handleVerifyEvent = function( event ) {
  if( event.character === '\u0000' ) {
    event.doit = false; // prevent pasting
  }
};

var isNumber = function( character ) {
  var charCode = character.charCodeAt( 0 );
  return charCode >=48 && charCode <= 57;
};
