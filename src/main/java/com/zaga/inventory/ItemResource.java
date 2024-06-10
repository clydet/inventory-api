package com.zaga.inventory;

import com.zaga.inventory.model.Item;
import com.zaga.inventory.repository.ItemRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/items")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ItemResource {

    @Inject
    ItemRepository itemRepository;

    @GET
    public List<Item> getAllItems() {
        return itemRepository.listAll();
    }

    @POST
    @Transactional
    public Response addItem(Item item) {
        itemRepository.persist(item);
        return Response.ok(item).status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateItem(@PathParam("id") Long id, Item item) {
        Item entity = itemRepository.findById(id);
        if (entity == null) {
            throw new WebApplicationException("Item with id " + id + " not found", Response.Status.NOT_FOUND);
        }
        entity.setName(item.getName());
        entity.setDescription(item.getDescription());
        entity.setVendorId(item.getVendorId());
        itemRepository.persist(entity);
        return Response.ok(entity).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteItem(@PathParam("id") Long id) {
        boolean deleted = itemRepository.deleteById(id);
        if (!deleted) {
            throw new WebApplicationException("Item with id " + id + " not found", Response.Status.NOT_FOUND);
        }
        return Response.noContent().build();
    }

    @GET
    @Path("/{id}")
    public Item getItem(@PathParam("id") Long id) {
        Item item = itemRepository.findById(id);
        if (item == null) {
            throw new WebApplicationException("Item with id " + id + " not found", Response.Status.NOT_FOUND);
        }
        return item;
    }
}
