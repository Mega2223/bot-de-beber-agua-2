# bot-de-beber-agua-2

## Bot de utilidades que eu ando fazendo :)

Continuação do meu primeiro bot-de-beber-agua com mais coisas.
 O bot foi feito principalmente pra RN, mas ele funciona em outros servidores
 
 Pode pegar os códigos que vc quiser.

## Comandos

#### -entra 
Entra no seu canal de voz.

#### -vaza
Sai do seu canal de voz.

#### -toca [link]
Toca um som de um link especificado ou coloca ele na fila caso ele esteja tocando outro áudio.

#### -skip
Vai pro próximo vídeo na fila.

#### -volume [número]
Seta o volume do bot, qualquer volume acima de 100 só vai pra quem é especificado como 'trusteduser' no configs.properties

#### -curva (o restAPI vai atrasar as outras mensagens)
Faz uma curva de cosseno em progressão de 0,01.

#### -tiraogames
Comando de piada, eu tenho que tirar ele.

#### -deixaogames
Comando de piada, eu tenho que tirar ele.

#### -mega [mensagem]
Coloca um texto na tela de quem tá rodando o bot.

#### -mega [imagem]
Coloca uma imagem na tela de quem tá rodando o bot.

#### -pingpong [usuário]
Inicia uma partida de ping pong contra o user mencionado.

#### -pingpong [usuário] [intervalo entre mínimo e máximo] [número máximo de segundos entre jogadas]
Inicia uma partida de ping pong contra o user mencionado.

#### -pegaarquivo [nome]
Retorna um valor de string do arquivo com o nome indicado no diretório net/Mega2223/arquivosconfidenciais, se não houver nenhum arquivo o bot vai te chamar de otário.

#### -mandaarquivo [nome] [conteúdo]
Manda ou escreve num arquivo com o nome especificado dentro do diretório net/Mega2223/arquivosconfidenciais, se tu fez o comando errado o bot te chama de otário.

#### -escrevenoarquivo [nome] [conteúdo]
Manda ou escreve num arquivo com o nome especificado dentro do diretório net/Mega2223/arquivosconfidenciais, se tu fez o comando errado o bot te chama de otário.

#### -listaosarquivos
Fala todos os arquivos presentes no diretório net/Mega2223/arquivosconfidenciais.

#### -saysplit [nome] [sintaxe]
Pega um arquivo no diretório net/Mega2223/arquivosconfidenciais em formato String, e fala ele mensagem por mensagem utilizando a sintaxe como referência de onde separara as mensagens.

#### -enablelisteners (só pra quem tem user confiado)
Ativa listeners de atividade da República Nômade e manda toda a atividade pro canal determinado como canalDoBot na classe principal.

#### -disablelisteners (só pra quem tem user confiado)
Desativa listeners de atividade da República Nômade e manda toda a atividade pro canal determinado como canalDoBot na classe principal.

#### -givelogs (só pra quem tem user confiado)
Dá todos os logs que o bot tem armazenados.

#### -cleanlogs (só pra quem tem user confiado)
Reseta os logs.

#### -calaaboca [user] [nº de mensagens] (só pra quem tem user confiado)
Apaga as mensagens desse user, [nº de mensagens] é o nº de mensagens a serem escaneadas, não necessariamente a serem apagadas, máximo de 100 mensagens.

#### -1984 [users] (só pra quem tem user confiado)
Tira esse user do canal de voz, não deixa ele entrar em canais de voz e deleta todas suas mensagens novas instantaneamente.
Literalmente o pesadelo de Orwell.

#### -2020 [users] (só pra quem tem user confiado)
Reverte os efeitos do -1984.

#### -liquidifica [user] (só pra quem tem user confiado)
Faz com que o user mencionado seja arrastado por todos os canais de voz do server, e em seguida o coloca no canal de voz o qual ele começou.

#### -givepm (só pra quem tem user confiado)
Lê TODOS os chats que o bot conseguir, e dá um debug dos chats com mensagens.
Não recomendo vc fazer isso, o restAPI não gosta.

#### -manda [mensagem] (só pra quem tem user confiado)
O bot apaga sua mensagem e manda ela logo em seguida.

#### -move [id do canal] (só pra quem tem user confiado)
Move todo mundo no seu canal de voz pro canal especificado no [id do canal].

#### -addproperty[argumento] [valor] (só pra quem tem user confiado)
Coloca uma propriedade no configs.properties .

#### -removeproperty[argumento] (só pra quem tem user confiado)
Tira uma propriedade no configs.properties .

#### -setproperty[argumento] [valor](só pra quem tem user confiado)
Coloca uma propriedade no configs.properties .

#### -getproperties (só pra quem tem user confiado)
Te dá uma lista de todas as propriedades salvas no config.properties .

#### -giveservers (só pra quem tem user confiado)
Dá um reporte dos servers que o bot tá.

#### -report (só pra quem tem user confiado)
Dá um reporte dos servers que o bot tá com mais detalhes.

#### -shutdown (só eu posso usar)
Desliga o bot e a máquina virtual por completo.

#### -cleannotifiers (só eu posso usar)
Tira todos os popups gerados pelo -mega.

#### -webhook (só eu posso usar)
Cria um webhook, atualmente é meio quebrado.

## Problemas conhecidos:

~is comandos tão com uma sintaxe maluca

~else if pra caramba

~documentação tá horrível

~2020 era pra ser 2021 tecnicamente
