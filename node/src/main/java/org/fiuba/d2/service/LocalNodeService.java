package org.fiuba.d2.service;

import org.fiuba.d2.model.node.LocalNode;
import org.fiuba.d2.model.node.Token;
import org.fiuba.d2.persistence.ItemRepository;
import org.fiuba.d2.persistence.local.LocalToken;
import org.fiuba.d2.persistence.local.PersistedNode;
import org.fiuba.d2.persistence.local.PersistedNodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class LocalNodeService {

    private PersistedNodeRepository persistedNodeRepository;
    private ItemRepository itemRepository;


    public LocalNodeService(PersistedNodeRepository persistedNodeRepository, ItemRepository itemRepository) {
        this.persistedNodeRepository = persistedNodeRepository;
        this.itemRepository = itemRepository;
    }

    public void saveLocalNode(LocalNode node) {
        PersistedNode persistedNode = new PersistedNode();
        persistedNode.setNodeId(node.getId());
        persistedNode.setNodeName(node.getName());
        persistedNode.setUri(node.getUri());
        persistedNode.setTokens(node.getTokens().stream().map(t -> new LocalToken(t.getValue())).collect(toList()));
        persistedNodeRepository.save(persistedNode);
    }

    public LocalNode findLocalNode() {
        PersistedNode node = persistedNodeRepository.findAll().get(0);
        List<Token> tokens = node.getTokens().stream().map(t -> new Token(t.getValue())).collect(toList());
        return new LocalNode(node.getNodeId(), node.getNodeName(), node.getUri(), tokens, itemRepository);
    }
}
