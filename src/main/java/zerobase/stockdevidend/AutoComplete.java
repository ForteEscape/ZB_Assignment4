package zerobase.stockdevidend;

import org.apache.commons.collections4.Trie;
import org.apache.commons.collections4.trie.PatriciaTrie;

public class AutoComplete {
    private Trie trie;

    AutoComplete(Trie trie){
        this.trie = trie;
    }

    public void add(String s){
        trie.put(s, "world");
    }

    public Object get(String s){
        return trie.get(s);
    }
}
