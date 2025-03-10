package com.hanre.fakeinsta.util;

import com.hanre.fakeinsta.handler.ResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class TransformToDTO {

    private String sortBy = "";
    private String sort = "";

    /**
     Method untuk pagination
     format pengembalian data paging ada disini
     sesuaikan dengan kebutuhan di FE nanti
     */
    public ResponseEntity<Object> transformObject(Map<String,Object> mapz, List ls, Page page
            , String filterBy, String value, List componentFiltering, HttpServletRequest request)//<PENAMBAHAN 21-12-2023>
    {
        /**
         *  Mengambil informasi sortby berdasarkan dari object page yang diproses sebelumnya
         *  UNSORTED adalah default dari spring data JPA untuk menghandle value nya tidak di set dari program
         *  jadi kita gunakan "id" untuk default sortBy dan "asc" untuk default sort
         */
        sortBy = page.getSort().toString().split(":")[0];
        sortBy = sortBy.equals("UNSORTED")?"id":sortBy;
        sort   = sortBy.equals("UNSORTED")?"asc":page.getSort().toString().split(":")[1];
        mapz.put("content",ls);
        mapz.put("total_items",page.getTotalElements());
        mapz.put("page_number",page.getNumber());
        mapz.put("total_pages",page.getTotalPages());
        mapz.put("sort",sort.trim().toLowerCase());
        mapz.put("number_of_elements",page.getNumberOfElements());
        mapz.put("column_name",filterBy);
//        mapz.put("component-filter",componentFiltering);
        mapz.put("value",value);
        return new ResponseHandler().
                generateResponse("PERMINTAAN DATA BERHASIL",
                        HttpStatus.OK,
                        mapz,
                        null,
                        request);
    }

}
