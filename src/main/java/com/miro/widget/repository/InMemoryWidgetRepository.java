package com.miro.widget.repository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import com.miro.widget.exception.WidgetNotFoundException;
import com.miro.widget.model.Widget;
import org.springframework.stereotype.Repository;

@Repository
public class InMemoryWidgetRepository implements WidgetRepository {

  private final ConcurrentSkipListMap<Integer, Widget> widgetMap = new ConcurrentSkipListMap<>();
  private final Map<Long, Integer> zIndexMap = new ConcurrentHashMap<>();

  @Override
  public Widget create(Widget widget) {
    if (widget.getZIndex() == null) {
      widget.setZIndex(widgetMap.isEmpty() ? 0 : widgetMap.lastKey() + 1);
    }
    if (widgetMap.containsKey(widget.getZIndex())) {
      shiftExistingWidgets(widget.getZIndex());
    }
    widget.setLastModificationDate(LocalDate.now());
    put(widget);
    return widget.toBuilder().build();
  }

  private void shiftExistingWidgets(Integer startIndex) {
    Map<Integer, Widget> tailMap = widgetMap.tailMap(startIndex);
    Widget existingWidget = null;
    for (Widget nextWidget : tailMap.values()) {
      shiftWidget(existingWidget);
      if (existingWidget == null) { // only remove the first item, other items will be overridden immediately
        widgetMap.remove(startIndex);
      }
      if (existingWidget != null && nextWidget.getZIndex() > existingWidget.getZIndex()) {
        existingWidget = null;
        break;
      }
      existingWidget = nextWidget;
    }
    shiftWidget(existingWidget);
  }

  private void shiftWidget(Widget widget) {
    if (widget != null) {
      widget.setZIndex(widget.getZIndex() + 1);
      put(widget);
    }
  }

  private void put(Widget widget) {
    widgetMap.put(widget.getZIndex(), widget);
    zIndexMap.put(widget.getId(), widget.getZIndex());
  }

  @Override
  public void delete(Long id) {
    Widget widget = findById(id);
    delete(widget);
  }

  private void delete(Widget widget) {
    widgetMap.remove(widget.getZIndex());
    zIndexMap.remove(widget.getId());
  }

  @Override
  public Widget findById(Long id) {
    if (!zIndexMap.containsKey(id)) {
      throw new WidgetNotFoundException();
    }
    Integer zIndex = zIndexMap.get(id);
    return widgetMap.get(zIndex);
  }

  @Override
  public Collection<Widget> findAll() {
    return widgetMap.values();
  }

  @Override
  public Widget update(Long id, Widget widget) {
    Widget existingWidget = findById(id);
    if (existingWidget.getZIndex().equals(widget.getZIndex())) {
      widget.setLastModificationDate(LocalDate.now());
      widgetMap.replace(widget.getZIndex(), widget);
      return widget;
    } else {
      delete(existingWidget);
      return create(widget);
    }
  }

}
