function( ev ) { 

  var newCh = ev.character;
  var keyCode = ev.keyCode;
  var sel = ev.widget.getSelection()[ 0 ];
  var text = ev.widget.getText();

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
  
  var isNumber = function( character ) {
    var charCode = character.charCodeAt( 0 );
    return charCode >=48 && charCode <= 57;
  }
  
  if( keyCode !== SWT.ARROW_LEFT && keyCode !== SWT.ARROW_RIGHT ) {
    ev.doit = false;
  }

  if( text[ sel ] === "_" && isNumber( newCh ) ) {
    replaceNextChar( newCh );
    if( text[ sel ] === "." ) {
      sel++;
    }    
  } else if( keyCode === SWT.BS && sel > 0 ) {
    if( text[ sel - 1 ] === "." ) {
      sel--;
    }
    if( isNumber( text[ sel - 1 ] ) ) {
      replacePrevChar( "_" );
    } else {
      sel--;
    }
    ev.doit = false;
  }

  // TODO: Setting text also sets selection to last position - compare to SWT
  ev.widget.setText( text );
  ev.widget.setSelection( [ sel, sel ] );
};
