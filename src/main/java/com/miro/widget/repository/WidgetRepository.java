package com.miro.widget.repository;

import java.util.Collection;
import com.miro.widget.model.Widget;

public interface WidgetRepository {

  Widget create(Widget widget);

  void delete(Long id);

  Widget findById(Long id);

  Collection<Widget> findAll();

  Widget update(Long id, Widget widget);

}
