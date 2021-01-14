package org.rainday.swagger

import io.swagger.v3.core.util.Json
import io.swagger.v3.core.util.Yaml
import io.swagger.v3.oas.models.OpenAPI
import org.rainday.swagger.controller.ResourceInPackageA
import spock.lang.Specification

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
        Set<Class<?>> classes = new HashSet<>();
        classes.add(ResourceInPackageA.class)
        OpenAPI api = reader.read(classes);

        then:
        Yaml.pretty(api) != null
        println Yaml.pretty(api)
        // 左：actual 右：expect
        Yaml.pretty(api) == expectResult
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
