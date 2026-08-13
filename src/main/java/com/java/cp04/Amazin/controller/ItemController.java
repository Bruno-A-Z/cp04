package com.java.cp04.Amazin.controller;

import com.java.cp04.Amazin.assembler.ItemModelAssembler;
import com.java.cp04.Amazin.model.Item;
import com.java.cp04.Amazin.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/item")
@RequiredArgsConstructor
public class   ItemController {

    private final ItemService itemService;
    private final ItemModelAssembler assembler;

    @GetMapping
    public CollectionModel<EntityModel<Item>> listarTodos() {
        List<EntityModel<Item>> itens = itemService.listarTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(itens,
                linkTo(methodOn(ItemController.class).listarTodos()).withSelfRel());
    }

    @GetMapping("/{id}")
    public EntityModel<Item> buscarPorId(@PathVariable Long id) {
        Item item = itemService.buscarPorId(id);
        return assembler.toModel(item);
    }

    @PostMapping
    public ResponseEntity<EntityModel<Item>> criar(@RequestBody Item item) {
        Item novoItem = itemService.criar(item);
        EntityModel<Item> entityModel = assembler.toModel(novoItem);

        return ResponseEntity
                .created(linkTo(methodOn(ItemController.class).buscarPorId(novoItem.getId())).toUri())
                .body(entityModel);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EntityModel<Item>> atualizar(@PathVariable Long id, @RequestBody Item item) {
        Item itemAtualizado = itemService.atualizar(id, item);
        return ResponseEntity.ok(assembler.toModel(itemAtualizado));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<EntityModel<Item>> atualizarParcial(@PathVariable Long id, @RequestBody Item item) {
        Item itemAtualizado = itemService.atualizarParcial(id, item);
        return ResponseEntity.ok(assembler.toModel(itemAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        itemService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}