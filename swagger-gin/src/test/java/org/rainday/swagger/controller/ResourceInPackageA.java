package org.rainday.swagger.controller;

import io.swagger.v3.oas.models.PathItem;
import java.util.List;
import java.util.Map;
import org.rainday.swagger.annotations.Consumes;
import org.rainday.swagger.annotations.FormParam;
import org.rainday.swagger.annotations.Path;
import org.rainday.swagger.methods.PATCH;
import org.rainday.swagger.model.Pet;

@Path("/packageA")
public class ResourceInPackageA {
    /*@Operation(operationId = "test.")
    @Path("sss")
    @GET
    public void getTest(@Parameter(name = "test") ArrayList<String> tenantId) {
        return;
    }
    
    @Operation(operationId = "testsss.")
    @Path("sss1")
    @POST
    public void getTestsss(@Parameter(name = "test") ArrayList<String> tenantId, @FormParam("pet") Pet pet) {
        return;
    }
    
    */
    
    /**
     * {@link PathItem#getSummary()}
     *
     * @param pet
     *//*
    @Operation(operationId = "testsss111.")
    @Path("sss1111")
    @POST
    public void getTestsss111(Pet pet) {
        return;
    }*/
    
    /*@Path("/addPet11")
    @POST
    public Response addPet(Pet pet, @Parameter(name = "a1", in = ParameterIn.HEADER) String a1, @PathParam("a2") String a2, @QueryParam("a3") String a3, int b, String[] strarray,
                           List<String> strlist) {
        
        return Response.ok().entity("SUCCESS").build();
    }*/
    
    @Path("/addPetsMap")
    @Consumes(value = "multipart/form-data")
    @PATCH
    public List<Map<String, Pet>> addPetsMap(@FormParam("a") String a, @FormParam("a1") List<String> a1 , @FormParam("pet") Pet pet) {
        return null;
    }
    /*
    @POST
    @Path("/addPetsSet")
    public Response addPetsSet(@FormParam("sets") Set<Pet> sets) {
    
        return Response.ok().entity("SUCCESS").build();
    }*/
}
