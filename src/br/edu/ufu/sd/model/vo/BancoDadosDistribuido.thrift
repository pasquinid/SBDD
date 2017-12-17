struct Vertice {
	1:i32 nome,
	2:i32 cor,
	3:string descricao
	4:i32 hash
}

struct ChaveAresta {
	1:i32 vertice1,
	2:i32 vertice2,
	3:i32 hash
}

struct Aresta {
	1:ChaveAresta chave,
	2:double peso,
	3:bool direcionada,
	4:string descricao
}

exception VerticeNaoEncontrado {

}

exception ArestaNaoEncontrada {

}

exception NaoExisteArestas {

}

exception NaoExisteVertices {

}

exception NoNaoEncontrado {

}

exception TabelaDeRoteamentoCheia {

}

exception ForaDosLimitesDaTabela {

}

struct No {
	1:i32 identificador,
	2:string endereco,
	3:i32 porta
}

service BancoDadosDistribuido {
	bool inserirVertice(1:Vertice vertice,2:i32 flag),
	Vertice getVertice(1:Vertice vertice),
	bool atualizarVertice(1:Vertice vertice),
	bool deletarVertice(1:Vertice vertice),
	
	bool inserirAresta(1:Aresta aresta),
	Aresta getAresta(1:ChaveAresta chaveAresta),
	bool atualizarAresta(1:Aresta aresta),
	bool deletarAresta(1:ChaveAresta chaveAresta),

	set<Vertice> getTodosVertices(1:set<No> nosVisitados),
	set<Aresta> getTodasArestas(1:set<No> nosVisitados),
	list<Aresta> getIncidentes(1:Vertice vertice),
	list<Vertice> getAdjacentes(1:Vertice vertice),

	No get(),
	
	bool adicionarNo(1:No no),
	bool adicionarNoIndice(1:i32 indice, 2:No no),
	bool removerNo(1:No no),
	bool removerNoIndice(1:i32 indice)
}
