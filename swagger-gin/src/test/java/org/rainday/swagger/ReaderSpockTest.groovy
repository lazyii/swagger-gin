package org.rainday.swagger

import io.swagger.v3.core.util.Json
import io.swagger.v3.core.util.Yaml
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.Paths
import org.rainday.swagger.controller.ResourceInPackageA
import org.rainday.swagger.picker.Reader
import spock.lang.Specification

import static org.hamcrest.CoreMatchers.equalTo
import static org.hamcrest.CoreMatchers.not
import static spock.util.matcher.HamcrestSupport.that
/**
 * Created by admin on 2020/12/30 10:06:57.
 */
class ReaderSpockTest extends Specification {

    def expectResult =
            '''openapi: 3.0.1
paths:
  /packageA/addPetsMap:
    patch:
      operationId: addPetsMap
      requestBody:
        content:
          multipart/form-data:
            schema:
              type: object
              properties:
                a:
                  type: string
                a1:
                  type: array
                  items:
                    type: string
                pet:
                  $ref: '#/components/schemas/Pet\'
      responses:
        default:
          description: default response
          content:
            '*/*':
              schema:
                type: array
                items:
                  type: object
                  additionalProperties:
                    $ref: '#/components/schemas/Pet\'
components:
  schemas:
    Category:
      type: object
      properties:
        id:
          type: integer
          format: int64
        name:
          type: string
      xml:
        name: Category
    Pet:
      type: object
      properties:
        id:
          type: integer
          format: int64
        category:
          $ref: '#/components/schemas/Category\'
        name:
          type: string
        photoUrls:
          type: array
          xml:
            wrapped: true
          items:
            type: string
            xml:
              name: photoUrl
        tags:
          type: array
          xml:
            wrapped: true
          items:
            $ref: '#/components/schemas/Tag\'
        status:
          type: string
          description: pet status in the store
          enum:
          - "available,pending,sold"
      xml:
        name: Pet
    Tag:
      type: object
      properties:
        id:
          type: integer
          format: int64
        name:
          type: string
      xml:
        name: Tag
''';

    def "reader simple yaml test"() {
        given:
        Reader reader = new Reader();


        when:
        Paths p1 = new Paths();
        System.out.println(p1 == new Paths());

        Set<Class<?>> classes = new HashSet<>();
        classes.add(ResourceInPackageA.class)
        OpenAPI api = reader.read(classes);

        then:
        that null, not(Yaml.pretty(api))
        println Yaml.pretty(api)
        // 左：expect 右：actual
        that expectResult, equalTo(Yaml.pretty(api))
    }

    def "reader simple json test"() {
        given:
        Reader reader = new Reader();

        when:
        OpenAPI api = reader.read(ResourceInPackageA.class);

        then:
        Json.pretty(api) != null
        System.out.println(Json.pretty(api))
    }
}
