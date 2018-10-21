package org.fiuba.d2.model.node;

import org.fiuba.d2.model.Item;
import org.fiuba.d2.persistence.ItemRepository;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.List;

import static java.util.Objects.nonNull;
import static org.fiuba.d2.utils.HashGenerator.sha1;
import static org.fiuba.d2.utils.StringUtils.getHexString;

@Entity
@DiscriminatorValue("LOCAL")
public class LocalNode extends NodeImpl {

    @Transient
    private ItemRepository repository;

    private LocalNode() {
    }

    public LocalNode(String name, String uri, List<Token> tokens, ItemRepository repository) {
        super(name, uri, NodeStatus.AVAILABLE, tokens);
        this.repository = repository;
    }

    @Override
    public void put(String key, String value) {
        repository.saveAndFlush(new Item(getHexString(sha1(key)), key, value));
    }

    @Override
    public String get(String key) {
        Item one = repository.findOne(getHexString(sha1(key)));
        return nonNull(one) ? one.getValue() : null;
    }
}
