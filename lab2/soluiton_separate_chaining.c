#include <stdio.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <stdbool.h>

#define MWL 110

// using nodes for separate chaining
typedef struct Node Node;
struct Node {
    char word[MWL];
    uint32_t hash_code;
    uint32_t count;
    Node* next;
};

typedef struct {
    size_t nbr_in_ht;
    size_t size;
    Node** elems;
} HashTable;

uint32_t hashpjw(void* sp)
{
    const char* id = sp;
    const char* p;
    uint32_t h = 0;
    uint32_t g;

    for (p = id; *p; ++p) {
        h = (h << 4) + (unsigned char)(*p);
        if ((g = h & 0xF0000000)) {
            h ^= g >> 24;
            h ^= g;
        }
    }
    return h;
}

HashTable* HashTable_create(size_t size)
{
    HashTable* ht = malloc(sizeof(HashTable));
    ht->size = size;
    ht->nbr_in_ht = 0;
    ht->elems = malloc(sizeof(Node*)*size);
    for (size_t i = 0; i < size; i++) {
       ht->elems[i] = NULL;
    }
    return ht;
}

Node* hashTable_get(HashTable* ht, char word[]) {
    // returns null if the thing isn't present.
    uint32_t hc = hashpjw(word);
    size_t idx = hc % ht->size;

    for (Node* curr = ht->elems[idx]; curr != NULL; curr = curr->next) {
        if (hc == curr->hash_code && strcmp(word, curr->word) == 0) return curr;
    }
    return NULL;
}

void hashTable_put(HashTable* ht, char word[]) {
    // if the nodes' elements are the same, update count,
    // otherwise go to next node until null and put it there
    ht->nbr_in_ht++;
    size_t hc = hashpjw(word);
    size_t idx = hc % ht->size;

    Node* curr = ht->elems[idx];
    if (curr == NULL) {
        curr = malloc(sizeof(Node));
        strcpy(curr->word, word);
        curr->count = 1;
        curr->hash_code = hc;
        curr->next = NULL;
        ht->elems[idx] = curr;
        return;
    }
    for (; curr != NULL; curr = curr->next) {
        // this should never happen because of the checks in the main loop
        // if (curr->count != 0 && curr->hash_code == hc) {
            // // in a normal hashmap this should override the prev.
            // curr->count++;
            // return;
        // }
        if (curr->next == NULL) {
            Node *new = malloc(sizeof(Node));
            strcpy(new->word, word);
            new->next = NULL;
            new->count = 1;
            new->hash_code = hc;
            curr->next = new;
            return;
        }
    }
}

void HashTable_delete(HashTable* ht, char word[]) {
    ht->nbr_in_ht--;
    size_t hc = hashpjw(word);
    size_t idx = hc % ht->size;

    Node* curr = ht->elems[idx];
    Node* prev = NULL;

    while (curr != NULL) {
        if (curr->hash_code == hc && strcmp(word, curr->word) == 0) {
            if (prev == NULL) {
                // first element
                ht->elems[idx] = curr->next;
            } else {
                prev->next = curr->next;
            }
            free(curr);
            return;
        }
        prev = curr;
        curr = curr->next;
    }
}

void hashTable_free(HashTable* ht) {
    for (size_t i = 0; i < ht->size; i++) {
        Node *curr = ht->elems[i];
        while (curr)
        {
            Node *tmp = curr;
            curr = curr->next;
            free(tmp);
        }
    }
    free(ht->elems);
    free(ht);
}

Node* getNodeWithMaxCount(HashTable* ht) {
    // if there are ties, use the one with the lowest alphabetical order
    Node* maxNode = NULL;

    for (size_t i = 0; i < ht->size; i++) {
        for (Node* curr = ht->elems[i]; curr != NULL; curr = curr->next) {
            if (maxNode == NULL ||
                curr->count > maxNode->count ||
                (curr->count == maxNode->count &&
                    strcmp(curr->word, maxNode->word) < 0)
            ) {
                maxNode = curr;
            }
        }
    }

    return maxNode;
}

HashTable* doubleSize(HashTable* ht) {
    HashTable* new_ht = HashTable_create(ht->size*2);

    for (size_t i = 0; i < ht->size; i++)
    {
        for (Node *curr = ht->elems[i]; curr != NULL; curr = curr->next)
        {
            hashTable_put(new_ht, curr->word);
            hashTable_get(new_ht, curr->word)->count = curr->count;
        }
    }
    hashTable_free(ht);
    return new_ht;
}

int main(int argc, char* argv)
{
    HashTable* ht = HashTable_create(1);

    char word[MWL];
    int i = 0;
    while (fgets(word, sizeof(word), stdin)) {
        // change word to null terminator
        word[strcspn(word, "\n")] = '\0';

        Node* curr = hashTable_get(ht, word);
        bool is_present = curr != NULL;
        bool remove_it = (i % 16 == 0);

        if (is_present) {
            if (remove_it) HashTable_delete(ht, word);
            else curr->count++;
        }
        else if (!remove_it) hashTable_put(ht, word);
        i++;

        if (ht->nbr_in_ht != 0 && ((float) ht->nbr_in_ht/ht->size) > 0.25) {
            ht = doubleSize(ht);
        }
    }
    // get word with largest count and print it
    Node* max = getNodeWithMaxCount(ht);
    if (max != NULL) printf("%s %d", max->word, max->count);

    hashTable_free(ht);
}
