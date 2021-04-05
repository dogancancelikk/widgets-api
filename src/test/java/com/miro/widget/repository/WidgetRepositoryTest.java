package com.miro.widget.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.assertThrows;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import com.miro.widget.exception.WidgetNotFoundException;
import com.miro.widget.model.Widget;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class WidgetRepositoryTest {

  private WidgetRepository repository;

  @BeforeEach
  void setup() {
    repository = new InMemoryWidgetRepository();
  }

  @Test
  void create_emptyZIndex_assignZIndex() {
    Widget widgetRequest = Widget.builder()
      .id(1L)
      .build();
    Widget firstWidget = repository.create(widgetRequest);
    widgetRequest = Widget.builder()
      .id(2L)
      .build();
    Widget secondWidget = repository.create(widgetRequest);

    assertThat(firstWidget.getZIndex()).isZero();
    assertThat(secondWidget.getZIndex()).isEqualTo(1);
    assertThat(firstWidget.getLastModificationDate()).isToday();
  }

  @Test
  void create_existingZIndex_shiftExistingValues() {
    Widget widgetRequest = Widget.builder()
      .id(1L)
      .zIndex(5)
      .build();
    repository.create(widgetRequest);
    widgetRequest = Widget.builder()
      .id(2L)
      .zIndex(6)
      .build();
    repository.create(widgetRequest);
    widgetRequest = Widget.builder()
      .id(4L)
      .zIndex(8)
      .build();
    repository.create(widgetRequest);
    widgetRequest = Widget.builder()
      .id(3L)
      .zIndex(5)
      .build();
    Widget thirdWidget = repository.create(widgetRequest);

    assertSoftly(softly -> {
        softly.assertThat(thirdWidget.getZIndex()).isEqualTo(5);
        softly.assertThat(repository.findById(1L).getZIndex()).isEqualTo(6);
        softly.assertThat(repository.findById(2L).getZIndex()).isEqualTo(7);
        softly.assertThat(repository.findById(4L).getZIndex()).isEqualTo(8);
      }
    );
  }

  @Test
  void delete_validId_deleteWidget() {
    Widget widgetRequest = Widget.builder()
      .id(1L)
      .build();
    repository.create(widgetRequest);
    repository.delete(widgetRequest.getId());

    assertThrows(WidgetNotFoundException.class, () -> repository.findById(1L));
  }


  @Test
  void findById_invalidId_throwException() {
    assertThrows(WidgetNotFoundException.class, () -> repository.findById(1L));
  }


  @Test
  void findById_validId_returnWidget() {
    Widget widgetRequest = Widget.builder()
      .id(1L)
      .zIndex(2)
      .length(3)
      .width(4)
      .xCoordinate(5)
      .yCoordinate(6)
      .build();
    repository.create(widgetRequest);
    Widget createdWidget = repository.findById(widgetRequest.getId());

    assertThat(createdWidget).usingRecursiveComparison().isEqualTo(widgetRequest);
  }

  @Test
  void findAll_noWidgetExists_returnEmpty() {
    Collection<Widget> widgets = repository.findAll();
    assertThat(widgets).isEmpty();
  }

  @Test
  void findAll_fourWidgetsExist_returnOrderedByZIndex() {
    Widget widgetRequest = Widget.builder()
      .id(1L)
      .zIndex(5)
      .build();
    repository.create(widgetRequest);
    widgetRequest = Widget.builder()
      .id(2L)
      .zIndex(6)
      .build();
    repository.create(widgetRequest);
    widgetRequest = Widget.builder()
      .id(4L)
      .zIndex(8)
      .build();
    repository.create(widgetRequest);
    widgetRequest = Widget.builder()
      .id(3L)
      .zIndex(5)
      .build();
    repository.create(widgetRequest);

    Collection<Widget> widgets = repository.findAll();

    assertThat(widgets).hasSize(4);
    AtomicInteger expectedIndex = new AtomicInteger(5);
    widgets.iterator().forEachRemaining(widget ->
      assertThat(widget.getZIndex()).isEqualTo(expectedIndex.getAndIncrement())
    );
  }

  @Test
  void update_sameZIndex_update() {
    Widget widgetRequest = Widget.builder()
      .id(1L)
      .zIndex(2)
      .length(3)
      .width(4)
      .xCoordinate(5)
      .yCoordinate(6)
      .build();
    repository.create(widgetRequest);
    widgetRequest.setLength(9);
    repository.update(widgetRequest.getId(), widgetRequest);

    Widget updatedWidget = repository.findById(widgetRequest.getId());

    assertThat(updatedWidget).usingRecursiveComparison().isEqualTo(widgetRequest);
    assertThat(updatedWidget.getLastModificationDate()).isToday();
  }

  @Test
  void update_differentZIndex_deleteInsert() {
    Widget widgetRequest = Widget.builder()
      .id(1L)
      .zIndex(2)
      .length(3)
      .width(4)
      .xCoordinate(5)
      .yCoordinate(6)
      .build();
    repository.create(widgetRequest);
    widgetRequest.setZIndex(5);
    repository.update(widgetRequest.getId(), widgetRequest);

    Widget updatedWidget = repository.findById(widgetRequest.getId());

    assertThat(updatedWidget).usingRecursiveComparison().isEqualTo(widgetRequest);
  }

}