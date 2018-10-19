package org.fiuba.d2.service;

import org.fiuba.d2.model.Item;
import org.fiuba.d2.model.Token;
import org.fiuba.d2.model.membership.MembershipEvent;
import org.fiuba.d2.model.membership.MembershipEventType;
import org.fiuba.d2.persistence.ItemRepository;
import org.fiuba.d2.persistence.MembershipEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.fiuba.d2.utils.HashGenerator.sha1;
import static org.fiuba.d2.utils.StringUtils.getHexString;

@Component
public class DHTService {

    private final ItemRepository itemRepository;
    private final MembershipEventRepository membershipEventRepository;

    @Autowired
    public DHTService(ItemRepository itemRepository, MembershipEventRepository membershipEventRepository) {
        this.itemRepository = itemRepository;
        this.membershipEventRepository = membershipEventRepository;
    }

    public void put(String key, String value) {
        itemRepository.save(convert(key, value));
    }

    public String get(String key) {


        List<Token> tokens = new ArrayList<>();
        tokens.add(Token.TokenBuilder.createRandom());
        tokens.add(Token.TokenBuilder.createRandom());
        tokens.add(Token.TokenBuilder.createRandom());
        tokens.add(Token.TokenBuilder.createRandom());
        tokens.add(Token.TokenBuilder.createRandom());
        MembershipEvent event = new MembershipEvent(System.currentTimeMillis(), MembershipEventType.ADD, "falopa", tokens);
        membershipEventRepository.saveAndFlush(event);


        Item one = itemRepository.findOne(getHexString(sha1(key)));
        return nonNull(one) ? one.getValue() : null;
    }

    private Item convert(String key, String value) {
        return new Item(getHexString(sha1(key)), key, value);
    }
}
