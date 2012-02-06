# Documentation

Currently hosted in the RAP Wiki: http://wiki.eclipse.org/RAP/ClientScripting

# Widget Object Reference

This is a temporary list of supported setter/getter on the ClientScripting Widget Object.
This will later be replaced by the official RAP Protocol property reference:
http://wiki.eclipse.org/RAP/Protocol#Object_Type_Reference

## Available Setter:
* <b>org.eclipse.swt.widgets.Control</b>: setTabIndex, setToolTip, setVisibility, setEnabled, setForeground, setBackground, setBackgroundImage, setCursor
* <b>org.eclipse.swt.widgets.CoolBar</b>: setLocked, setToolTip, setVisibility, setEnabled, setForeground, setBackground
* <b>org.eclipse.swt.widgets.Label</b>: setText, setImage, setAlignment, setEnabled
* <b>org.eclipse.swt.widgets.Menu</b>: setBounds, setEnabled
* <b>org.eclipse.swt.widgets.MenuItem</b>: setMenu, setEnabled, setText, setImage, setSelection
* <b>org.eclipse.swt.widgets.DropTarget</b>: setTransfer
* <b>org.eclipse.swt.widgets.DragSource</b>: setTransfer
* <b>org.eclipse.swt.widgets.Tree</b>: setItemCount, setItemHeight, setItemMetrics, setColumnCount, setTreeColumn, setFixedColumns, setHeaderHeight, setHeaderVisible
* <b>org.eclipse.swt.widgets.TreeItem</b>: setItemCount, setTexts, setImages, setBackground, setForeground, setFont, setCellBackgrounds, setCellForegrounds
* <b>org.eclipse.swt.widgets.TableColumn</b>: setIndex, setLeft, setWidth, setText, setImage, setToolTip, setResizable, setMoveable
* <b>org.eclipse.swt.widgets.Browser</b>: setUrl, setFunctionResult, setVisibility, setEnabled, setForeground
* <b>org.eclipse.swt.widgets.ExternalBrowser</b>: 
* <b>org.eclipse.swt.widgets.Group</b>: setText, setToolTip, setVisibility, setEnabled, setForeground, setBackground
* <b>org.eclipse.swt.widgets.Shell</b>: setShowMinimize, setAllowMinimize, setShowMaximize, setAllowMaximize, setShowClose, setAllowClose, setResizable, setImage
* <b>org.eclipse.swt.widgets.ProgressBar</b>: setMinimum, setMaximum, setSelection, setState
* <b>org.eclipse.swt.widgets.Link</b>: setText, setToolTip, setVisibility, setEnabled, setForeground, setBackground
* <b>org.eclipse.swt.widgets.ScrolledComposite</b>: setOrigin, setContent, setShowFocusedControl, setScrollBarsVisible
* <b>org.eclipse.swt.widgets.ToolBar</b>: setTabIndex, setToolTip, setVisibility, setEnabled, setForeground, setBackground, setBackgroundImage
* <b>org.eclipse.swt.widgets.ToolItem</b>: setBounds, setVisible, setEnabled, setHotImage
* <b>org.eclipse.swt.widgets.Scale</b>: setMinimum, setMaximum, setSelection, setIncrement, setPageIncrement
* <b>org.eclipse.swt.widgets.Combo</b>: setItemHeight, setVisibleItemCount, setItems, setListVisible, setSelectionIndex, setEditable, setText, setSelection
* <b>org.eclipse.swt.widgets.CLabel</b>: setText, setImage, setAlignment, setLeftMargin, setTopMargin, setRightMargin, setBottomMargin, setBackgroundGradient
* <b>org.eclipse.swt.widgets.Composite</b>: setBackgroundGradient, setRoundedBorder, setVisibility, setEnabled, setForeground
* <b>org.eclipse.swt.widgets.Sash</b>: setTabIndex, setToolTip, setVisibility, setEnabled, setForeground, setBackground, setBackgroundImage
* <b>org.eclipse.swt.widgets.Canvas</b>: setBackgroundGradient, setRoundedBorder, setVisibility, setEnabled, setForeground
* <b>org.eclipse.swt.widgets.List</b>: setItems, setSelectionIndices, setTopIndex, setFocusIndex, setScrollBarsVisible, setItemDimensions
* <b>org.eclipse.swt.widgets.TabFolder</b>: setSelection, setToolTip, setVisibility, setEnabled, setForeground, setBackground
* <b>org.eclipse.swt.widgets.TabItem</b>: setText, setImage, setControl, setToolTip
* <b>org.eclipse.swt.widgets.CoolItem</b>: setBounds, setControl
* <b>org.eclipse.swt.widgets.Button</b>: setText, setAlignment, setImage, setSelection, setGrayed
* <b>org.eclipse.swt.widgets.FileUpload</b>: setText, setImage, setVisibility, setEnabled, setForeground
* <b>org.eclipse.swt.widgets.Slider</b>: setMinimum, setMaximum, setSelection, setIncrement, setPageIncrement, setThumb
* <b>org.eclipse.swt.widgets.Spinner</b>: setMinimum, setMaximum, setSelection, setDigits, setIncrement, setPageIncrement, setTextLimit, setDecimalSeparator
* <b>org.eclipse.swt.widgets.DateTime</b>: setYear, setMonth, setDay, setHours, setMinutes, setSeconds, setSubWidgetsBounds
* <b>org.eclipse.swt.widgets.CTabItem</b>: setBounds, setFont, setText, setImage, setToolTip
* <b>org.eclipse.swt.widgets.CTabFolder</b>: setTabPosition, setTabHeight, setMinMaxState, setMinimizeBounds, setMinimizeVisible, setMaximizeBounds, setMaximizeVisible, setChevronBounds
* <b>org.eclipse.swt.widgets.ExpandItem</b>: setBounds, setText, setImage, setExpanded, setHeaderHeight
* <b>org.eclipse.swt.widgets.ExpandBar</b>: setBottomSpacingBounds, setVScrollBarVisible, setVScrollBarMax, setEnabled
* <b>org.eclipse.swt.widgets.Text</b>: setText, setMessage, setEchoChar, setEditable, setSelection, setTextLimit
* <b>org.eclipse.swt.widgets.Separator</b>: setTabIndex, setToolTip, setVisibility, setEnabled, setForeground, setBackground, setBackgroundImage
* <b>org.eclipse.swt.widgets.ControlDecorator</b>: setBounds, setText, setImage, setVisible, setShowHover
* <b>org.eclipse.swt.widgets.ToolTip</b>: setRoundedBorder, setBackgroundGradient, setAutoHide, setText, setMessage, setLocation, setVisible

## Available Getter:
* <b>org.eclipse.swt.widget.Text</b>: getText,getSelection