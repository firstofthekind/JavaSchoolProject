package com.firstofthekind.javaschoolproject.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartDto {

    public ItemDto itemDto;

    public void addItem(ItemDto itemDto) {
        this.itemDto = new ItemDto();
    }

    public void deleteItem() {
        itemDto = null;
    }

}
