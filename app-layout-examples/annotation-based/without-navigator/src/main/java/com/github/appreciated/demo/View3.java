package com.github.appreciated.demo;

import com.github.appreciated.app.layout.annotations.MenuCaption;
import com.github.appreciated.app.layout.annotations.MenuIcon;
import com.github.appreciated.app.layout.annotations.NavigatorViewName;
import com.vaadin.icons.VaadinIcons;

@NavigatorViewName("view3") // the path under which you can navigate to it
@MenuCaption("View 3")
@MenuIcon(VaadinIcons.CONNECT)
public class View3 extends ExampleView {
    @Override
    String getViewName() {
        return getClass().getName();
    }
}
