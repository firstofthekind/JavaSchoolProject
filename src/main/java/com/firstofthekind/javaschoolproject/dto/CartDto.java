package com.firstofthekind.javaschoolproject.dto;

import lombok.Getter;
import lombok.Setter;

import javax.mail.FetchProfile;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartDto {
    private List<ItemDto> itemDtoList = new ArrayList<>();

    private void addItem(ItemDto itemDto) {
        itemDtoList.add(itemDto);
    }

    private void deleteItem(Long contractID) {
        for (ItemDto item : itemDtoList) {
            if (item.getContractDto().getId() == contractID) {
                itemDtoList.remove(item);
            }
        }
    }

}
