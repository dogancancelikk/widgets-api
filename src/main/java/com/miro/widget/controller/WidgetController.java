package com.miro.widget.controller;

import java.net.URI;
import java.util.Collection;
import javax.validation.Valid;
import com.miro.widget.dto.PageRequest;
import com.miro.widget.dto.SaveWidgetRequest;
import com.miro.widget.dto.WidgetDto;
import com.miro.widget.service.WidgetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequiredArgsConstructor
public class WidgetController {

  private final WidgetService widgetService;

  @PostMapping("/widgets")
  public ResponseEntity<WidgetDto> createWidget(@Valid @RequestBody SaveWidgetRequest widget) {
    WidgetDto createdWidget = widgetService.create(widget);

    URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(createdWidget.getId())
      .toUri();
    return ResponseEntity.created(uri)
      .body(createdWidget);
  }

  @DeleteMapping("/widgets/{id}")
  public ResponseEntity<?> deleteWidget(@PathVariable("id") Long id) {
    widgetService.delete(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/widgets")
  public ResponseEntity<Collection<WidgetDto>> listWidgets(@Valid PageRequest pageRequest) {
    return ResponseEntity.ok(widgetService.findAll(pageRequest));
  }

  @GetMapping("/widgets/{id}")
  public ResponseEntity<WidgetDto> getWidget(@PathVariable("id") Long id) {
    return ResponseEntity.ok(widgetService.findById(id));
  }


  @PutMapping("/widgets/{id}")
  public ResponseEntity<WidgetDto> updateWidget(@PathVariable Long id, @Valid @RequestBody SaveWidgetRequest widget) {
    return ResponseEntity.ok(widgetService.update(id, widget));
  }

}
