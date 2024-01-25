const {transform} = require('vector-drawable-svg')
const svg_to_png  = require('svg-to-png')

const path = require('path')
const fs   = require('fs')

const dirRes = path.normalize(
                 path.join(__dirname, '..', '..', '..', 'android-studio-project', 'Hauk', 'src', 'main', 'res')
               )
const dirXml = path.join(dirRes, 'drawable-v24')
const dirPng = path.join(dirRes, 'drawable')
const dirSvg = path.join(dirPng, 'svg')

const readText  = (filepath)       => fs.readFileSync(filepath, 'utf8')
const writeText = (filepath, text) => fs.writeFileSync(filepath, text, 'utf8')

fs.mkdirSync(dirSvg)

fs.readdirSync(dirXml).forEach(filename => {
  if (filename.toLowerCase().endsWith('.xml')) {
    const fileXml = path.join(dirXml, filename)
    const fileSvg = path.join(dirSvg, filename.substring(0, filename.length - 3) + 'svg')
    const textXml = readText(fileXml)

    const textSvg = transform(textXml, {
      override: {
        '?attr/colorControlNormal': '#DE000000',
        '?attr/editTextColor':      '#DE000000',
        '?attr/colorAccent':        '#D80037'
      }
    })

    writeText(fileSvg, textSvg)
  }
})

svg_to_png.convert(dirSvg, dirPng)
