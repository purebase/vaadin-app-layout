package com.github.appreciated.app.layout.behaviour.left;

import com.github.appreciated.app.layout.behaviour.AppLayoutComponent;
import com.github.appreciated.app.layout.behaviour.listener.AppLayoutResizeListener;
import com.github.appreciated.app.layout.builder.design.AppBarDesign;
import com.github.appreciated.app.layout.builder.elements.*;
import com.github.appreciated.app.layout.builder.interfaces.ComponentProvider;
import com.github.appreciated.app.layout.builder.interfaces.NavigationElementComponent;
import com.github.appreciated.app.layout.builder.providers.left.DefaultLeftClickableNavigationElementProvider;
import com.github.appreciated.app.layout.builder.providers.left.DefaultLeftNavigationBadgeElementComponentProvider;
import com.github.appreciated.app.layout.builder.providers.left.DefaultLeftSectionElementComponentProvider;
import com.github.appreciated.app.layout.builder.providers.left.DefaultLeftSubmenuNavigationElementProvider;
import com.github.appreciated.app.layout.builder.providers.top.DefaultTopClickableNavigationElementProvider;
import com.github.appreciated.app.layout.builder.providers.top.DefaultTopNavigationBadgeElementComponentProvider;
import com.github.appreciated.app.layout.builder.providers.top.DefaultTopSectionElementComponentProvider;
import com.github.appreciated.app.layout.builder.providers.top.DefaultTopSubmenuNavigationElementProvider;
import com.github.appreciated.app.layout.component.layout.VerticalFlexBoxLayout;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.io.IOException;
import java.util.List;

public abstract class AbstractLeftAppLayout extends CustomLayout implements AppLayoutComponent, AppLayoutResizeListener.AppLayoutResizedListener {

    private final Panel contentPanel = new Panel();

    private final VerticalLayout menuHeaderHolder = new VerticalLayout();
    private final VerticalLayout menuElementHolder = new VerticalLayout();
    private final VerticalLayout menuFooterHolder = new VerticalLayout();

    private final VerticalFlexBoxLayout menuHolder = new VerticalFlexBoxLayout(menuHeaderHolder, menuElementHolder, menuFooterHolder);

    private final HorizontalLayout appBar = new HorizontalLayout();
    private final HorizontalLayout appBarElementWrapper = new HorizontalLayout();
    private final HorizontalLayout appBarElementContainer = new HorizontalLayout();
    private Component title = new Label("");
    private final HorizontalLayout titleWrapper = new HorizontalLayout(title);
    private List<NavigatorNavigationElement> list;
    private ComponentProvider<NavigationElementComponent, NavigatorNavigationElement> drawerNavigationElementProvider = new DefaultLeftNavigationBadgeElementComponentProvider();
    private ComponentProvider<SubmenuNavigationElement.SubmenuComponent, SubmenuNavigationElement> drawerSubmenuElementProvider = new DefaultLeftSubmenuNavigationElementProvider();
    private ComponentProvider<Component, SectionNavigationElement> drawerSectionElementProvider = new DefaultLeftSectionElementComponentProvider();
    private ComponentProvider<Component, ClickableNavigationElement> drawerClickableElementProvider = new DefaultLeftClickableNavigationElementProvider();
    private ComponentProvider<NavigationElementComponent, NavigatorNavigationElement> topNavigationElementProvider = new DefaultTopNavigationBadgeElementComponentProvider();
    private ComponentProvider<Component, SectionNavigationElement> topSectionElementProvider = new DefaultTopSectionElementComponentProvider();
    private ComponentProvider<SubmenuNavigationElement.SubmenuComponent, SubmenuNavigationElement> topSubmenuElementProvider = new DefaultTopSubmenuNavigationElementProvider();
    private ComponentProvider<Component, ClickableNavigationElement> topClickableElementProvider = new DefaultTopClickableNavigationElementProvider();

    public AbstractLeftAppLayout(String filename) throws IOException {
        super(AbstractLeftAppLayout.class.getResourceAsStream(filename));
        setSizeFull();
        contentPanel.setSizeFull();
        contentPanel.addStyleName(ValoTheme.PANEL_BORDERLESS);
        menuHolder.setSizeFull();
        menuHolder.grow(menuElementHolder);
        menuHolder.setOverflowAuto(true);

        menuHeaderHolder.setVisible(false);
        menuFooterHolder.setVisible(false);
        menuHeaderHolder.setMargin(false);
        menuElementHolder.setMargin(new MarginInfo(true, false));
        menuFooterHolder.setMargin(new MarginInfo(false, false, true, false));
        menuElementHolder.setWidth(100, Unit.PERCENTAGE);
        addStyleNames("app-layout-behaviour-" + getStyleName(), "app-layout");
        addComponent(contentPanel, "content");
        addComponent(menuHolder, "menu-elements");
        addComponent(appBar, "app-bar-elements");
        appBar.addComponents(titleWrapper, new AppLayoutResizeListener(this), appBarElementWrapper);
        appBar.setExpandRatio(appBarElementWrapper, 1);
        appBar.setWidth(100, Unit.PERCENTAGE);
        appBar.setHeight(100, Unit.PERCENTAGE);
        appBarElementWrapper.setSpacing(false);
        appBarElementWrapper.setSizeFull();
        appBarElementWrapper.addComponentAsFirst(appBarElementContainer);
        appBarElementContainer.setHeight(100, Unit.PERCENTAGE);
        appBarElementWrapper.setComponentAlignment(appBarElementContainer, Alignment.TOP_RIGHT);
        titleWrapper.setHeight(100, Unit.PERCENTAGE);
        titleWrapper.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
    }

    public abstract String getStyleName();

    @Override
    public void setNavigatorNavigationElements(List<NavigatorNavigationElement> list) {
        this.list = list;
    }

    @Override
    public void refreshNavigationElementInfo() {
        if (list != null) {
            list.forEach(NavigatorNavigationElement::refreshInfo);
        }
    }

    public void addAppBarElement(Component component) {
        appBarElementContainer.addComponent(component);
        appBarElementContainer.setComponentAlignment(component, Alignment.MIDDLE_RIGHT);
    }

    public void setDesign(AppBarDesign design) {
        this.addStyleName(design.getStylename());
    }

    public HorizontalLayout getAppBar() {
        return appBar;
    }

    public HorizontalLayout getAppBarElementWrapper() {
        return appBarElementWrapper;
    }

    public Component getTitle() {
        return title;
    }

    public void setTitle(String title) {
        if (this.title instanceof Label) {
            ((Label) this.title).setValue(title);
        }
    }

    @Override
    public Component getTitleComponent() {
        return title;
    }

    public void setTitleComponent(Component compoent) {
        titleWrapper.replaceComponent(this.title, compoent);
        this.title = compoent;
        titleWrapper.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
    }

    @Override
    public Panel getContentHolder() {
        return contentPanel;
    }

    public HorizontalLayout getTitleWrapper() {
        return titleWrapper;
    }

    public VerticalLayout getMenuElementHolder() {
        return menuElementHolder;
    }

    public VerticalLayout getMenuFooterHolder() {
        return menuFooterHolder;
    }

    public VerticalLayout getMenuHeaderHolder() {
        return menuHeaderHolder;
    }

    public Layout getMenuHolder() {
        return menuHolder;
    }

    public void addAppBarIcon(Component appBarIconComponent) {
        titleWrapper.addComponentAsFirst(appBarIconComponent);
        titleWrapper.setComponentAlignment(appBarIconComponent, Alignment.MIDDLE_LEFT);
    }

    @Override
    public void addNavigationElement(AbstractNavigationElement component) {
        component.setProvider(this);
        addToDrawer(component.getComponent());
    }

    @Override
    public ComponentProvider<Component, SectionNavigationElement> getDrawerSectionElementProvider() {
        return drawerSectionElementProvider;
    }

    @Override
    public void setDrawerSectionElementProvider(ComponentProvider<Component, SectionNavigationElement> provider) {
        drawerSectionElementProvider = provider;
    }

    @Override
    public ComponentProvider<Component, SectionNavigationElement> getTopSectionElementProvider() {
        return topSectionElementProvider;
    }

    @Override
    public void setTopSectionElementProvider(ComponentProvider<Component, SectionNavigationElement> provider) {
        topSectionElementProvider = provider;
    }

    @Override
    public ComponentProvider<SubmenuNavigationElement.SubmenuComponent, SubmenuNavigationElement> getDrawerSubmenuElementProvider() {
        return drawerSubmenuElementProvider;
    }

    @Override
    public void setDrawerSubmenuElementProvider(ComponentProvider<SubmenuNavigationElement.SubmenuComponent, SubmenuNavigationElement> provider) {
        drawerSubmenuElementProvider = provider;
    }

    @Override
    public ComponentProvider<SubmenuNavigationElement.SubmenuComponent, SubmenuNavigationElement> getTopSubmenuElementProvider() {
        return topSubmenuElementProvider;
    }

    @Override
    public void setTopSubmenuElementProvider(ComponentProvider<SubmenuNavigationElement.SubmenuComponent, SubmenuNavigationElement> provider) {
        topSubmenuElementProvider = provider;
    }

    @Override
    public ComponentProvider<NavigationElementComponent, NavigatorNavigationElement> getDrawerNavigationElementProvider() {
        return drawerNavigationElementProvider;
    }

    @Override
    public void setDrawerNavigationElementProvider(ComponentProvider<NavigationElementComponent, NavigatorNavigationElement> provider) {
        drawerNavigationElementProvider = provider;
    }

    @Override
    public ComponentProvider<NavigationElementComponent, NavigatorNavigationElement> getTopNavigationElementProvider() {
        return topNavigationElementProvider;
    }

    @Override
    public void setTopNavigationElementProvider(ComponentProvider<NavigationElementComponent, NavigatorNavigationElement> provider) {
        topNavigationElementProvider = provider;
    }

    @Override
    public ComponentProvider<Component, ClickableNavigationElement> getTopClickableElementProvider() {
        return topClickableElementProvider;
    }

    @Override
    public void setTopClickableElementProvider(ComponentProvider<Component, ClickableNavigationElement> topClickableElementProvider) {
        this.topClickableElementProvider = topClickableElementProvider;
    }

    @Override
    public ComponentProvider<Component, ClickableNavigationElement> getDrawerClickableElementProvider() {
        return drawerClickableElementProvider;
    }

    @Override
    public void setDrawerClickableElementProvider(ComponentProvider<Component, ClickableNavigationElement> drawerClickableElementProvider) {
        this.drawerClickableElementProvider = drawerClickableElementProvider;
    }

    @Override
    public void addNavigationFooterElement(AbstractNavigationElement component) {
        component.setProvider(this);
        addToDrawerFooter(component.getComponent());
    }

    @Override
    public void addNavigationHeaderElement(AbstractNavigationElement component) {
        component.setProvider(this);
        addToDrawerHeader(component.getComponent());
    }

    @Override
    public void addToDrawer(Component component) {
        menuElementHolder.addComponent(component);
    }

    @Override
    public void addToDrawerFooter(Component component) {
        menuFooterHolder.setVisible(true);
        menuFooterHolder.addComponent(component);
    }

    @Override
    public void addToDrawerHeader(Component component) {
        menuHeaderHolder.setVisible(true);
        menuHeaderHolder.addComponent(component);
    }

    @Override
    public void addToTop(Component component) {
        throw new UnsupportedOperationException("The Left Layout does not support this operation");
    }

    @Override
    public void addToTopFooter(Component component) {
        throw new UnsupportedOperationException("The Left Layout does not support this operation");
    }

    @Override
    public void addToTopHeader(Component component) {
        throw new UnsupportedOperationException("The Left Layout does not support this operation");
    }

    @Override
    public void onAppLayoutResized() {
        getUI().access(() -> getUI().markAsDirty());
    }
}
