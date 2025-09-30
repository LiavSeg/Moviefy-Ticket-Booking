import axios from 'axios';

export async function exportReport(payload, format = 'xml') {
  const isDownload = format === 'download';
  console.log(payload);
  return axios.post(`/xml/export?format=${format}`, payload, {
    responseType: isDownload ? 'blob' : 'text',
    headers: { 'Content-Type': 'application/json' },
    withCredentials: true
  });
}

export function getFilenameFromContentDisposition(header) {
  if (!header) return null;
  const match = /filename=([^;]+)/i.exec(header);
  if (!match) return null;
  const raw = match[1].replace(/["']/g, '');
  return raw.toLowerCase().endsWith('.xml') ? raw : `${raw}.xml`;
}
